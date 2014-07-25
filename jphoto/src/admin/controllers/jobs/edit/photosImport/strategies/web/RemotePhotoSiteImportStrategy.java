package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.ImageDiscEntry;
import admin.controllers.jobs.edit.photosImport.ImageToImport;
import admin.controllers.jobs.edit.photosImport.importParameters.AbstractImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.RemoteSitePhotosImportParameters;
import admin.controllers.jobs.edit.photosImport.strategies.AbstractPhotoImportStrategy;
import admin.jobs.entries.AbstractJob;
import admin.services.jobs.JobHelperService;
import core.exceptions.BaseRuntimeException;
import core.exceptions.SaveToDBException;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.log.LogHelper;
import core.services.photo.PhotoCommentService;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;
import core.services.utils.DateUtilsService;
import core.services.utils.RandomUtilsService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import utils.StringUtilities;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public class RemotePhotoSiteImportStrategy extends AbstractPhotoImportStrategy {

	public static final String REMOTE_PHOTO_SITE_USER_LOGIN_PREFIX = "PS_";

	private RemoteSitePhotosImportParameters importParameters;

	final LogHelper log = new LogHelper( RemotePhotoSiteImportStrategy.class );

	private final Date firstPhotoUploadTime;

	public RemotePhotoSiteImportStrategy( final AbstractJob job, final AbstractImportParameters parameters, final Services services ) {
		super( job, services, new LogHelper( RemotePhotoSiteImportStrategy.class ), parameters.getLanguage() );

		importParameters = ( RemoteSitePhotosImportParameters ) parameters;

		firstPhotoUploadTime = getServices().getJobHelperService().getFirstPhotoUploadTime();
	}

	@Override
	public void doImport() throws IOException, SaveToDBException {

		for ( final String remotePhotoSiteUserId : importParameters.getRemoteUserIds() ) {

			final RemotePhotoSiteUser remotePhotoSiteUser = new RemotePhotoSiteUser( remotePhotoSiteUserId );

			importRemotePhotoSiteUserPhotos( remotePhotoSiteUser );

			if( job.hasJobFinishedWithAnyResult() ) {
				break;
			}
		}
	}

	@Override
	public int getTotalOperations( final int totalJopOperations ) throws IOException {

		if ( totalJopOperations > 0 && totalJopOperations != AbstractJob.OPERATION_COUNT_UNKNOWN ) {
			return totalJopOperations;
		}

		int countedTotal = 0;

		final List<String> remotePhotoSiteUsersIds = importParameters.getRemoteUserIds();
		for ( final String remotePhotoSiteUserId : remotePhotoSiteUsersIds ) {

			final String userPageContent = importParameters.getRemoteContentHelper().getUserPageContent( 1, remotePhotoSiteUserId );

			if ( StringUtils.isEmpty( userPageContent ) ) {
				log.error( String.format( "ERROR getting remote photo site user #%s pages qty. Photos import of the user will be skipped.", remotePhotoSiteUserId ) );

				final TranslatableMessage translatableMessage = new TranslatableMessage( "ERROR getting remote photo site user #$1 pages qty. Photos import of the user will be skipped.", services )
					.string( remotePhotoSiteUserId )
					;
				job.addJobRuntimeLogMessage( translatableMessage );

				continue;
			}

			final int qty = getRemotePhotoSitePageContentDataExtractor().getTotalPagesQty( userPageContent, remotePhotoSiteUserId );
			countedTotal += qty;
			log.info( String.format( "Getting remote photo site user #%s pages qty: %d ( summary: %d )", remotePhotoSiteUserId, qty, countedTotal ) );

			if ( job.getGenerationMonitor().getStatus().isNotActive() ) {
				break;
			}
		}

		return countedTotal;
	}

	public static String createLoginForRemotePhotoSiteUser( final String remotePhotoSiteUserId ) {
		return String.format( "%s%s", REMOTE_PHOTO_SITE_USER_LOGIN_PREFIX, remotePhotoSiteUserId );
	}

	private void importRemotePhotoSiteUserPhotos( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException, SaveToDBException {

		final User user = findByNameOrCreateUser( remotePhotoSiteUser, importParameters );
		if ( user == null ) {
			log.error( String.format( "%s can not be created. Skipping user's photos import.", remotePhotoSiteUser ) );
			return;
		}

		prepareFolderForImageDownloading( remotePhotoSiteUser );

		final List<RemotePhotoSitePhoto> cachedLocallyRemotePhotoSitePhotos = getCachedLocallyRemotePhotoSitePhotos( remotePhotoSiteUser );

		int page = 1;

		final String userFirstPageContent = importParameters.getRemoteContentHelper().getUserPageContent( 1, remotePhotoSiteUser.getId() );
		if ( StringUtils.isEmpty( userFirstPageContent ) ) {
			return; // can not load page - just skipping
		}

		final int userPagesQty = getRemotePhotoSitePageContentDataExtractor().getTotalPagesQty( userFirstPageContent, remotePhotoSiteUser.getId() );

		while ( ! job.isFinished() && ! job.hasJobFinishedWithAnyResult() && page <= userPagesQty ) {

			log.debug( String.format( "Getting page %d context of %s", page, importParameters.getRemoteContentHelper().getUserCardLink( remotePhotoSiteUser ) ) );

			final String userPageContent = importParameters.getRemoteContentHelper().getUserPageContent( page, remotePhotoSiteUser.getId() );
			if ( StringUtils.isEmpty( userPageContent ) ) {

				log.info( "Can not load remote photo site user first page - skipping import user's photos" );

				continue;
			}

			final List<Integer> remotePhotoSitePagePhotosIds = extractUserPhotosIdsFromPage( userPageContent );

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			if ( remotePhotoSitePagePhotosIds.size() == 0 ) {
				final String userCardFileName = getUserCardFileName( remotePhotoSiteUser, page );
				final File file = getRemotePhotoSitePhotoImageFileUtils().writePageContentToFile( userCardFileName, userPageContent );

				final TranslatableMessage translatableMessage = new TranslatableMessage( "No photo have been found on page $1. User page content saved. See $2", services )
					.addIntegerParameter( page )
					.string( file.getAbsolutePath() )
					;
				job.addJobRuntimeLogMessage( translatableMessage );

				log.info( String.format( "No photo have been found on page %d. User page content saved. See %s", page, file.getCanonicalPath() ) );

				continue;
			}

			final RemotePhotoSitePhotosFromPageToImport remotePhotoSitePhotosToImport = getRemotePhotoSitePhotosToImport( remotePhotoSiteUser, remotePhotoSitePagePhotosIds, cachedLocallyRemotePhotoSitePhotos, user );
			final List<RemotePhotoSitePhoto> remotePhotoSitePagePhotos = remotePhotoSitePhotosToImport.getRemotePhotoSitePhotos();

			filterDownloadedPhotosByCategories( remotePhotoSitePagePhotos );

			final List<RemotePhotoSitePhotoDiskEntry> remotePhotoSitePhotoDiskEntries = getRemotePhotoSitePhotoDiskEntries( remotePhotoSiteUser, remotePhotoSitePagePhotos, cachedLocallyRemotePhotoSitePhotos );

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			final List<RemotePhotoSiteDBEntry> entries = preparePhotosToImport( remotePhotoSiteUser, user, remotePhotoSitePhotoDiskEntries );

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			final List<ImageToImport> imagesToImport = newArrayList();
			for ( final RemotePhotoSiteDBEntry dbEntry : entries ) {
				imagesToImport.add( dbEntry.getImageToImport() );
			}

			createPhotosDBEntries( imagesToImport );

			if ( importParameters.isImportComments() ) {
				importComments( entries );
			}

			if ( remotePhotoSitePhotosToImport.isBreakImportAfterThisPageProcessed() ) {
				job.increment( userPagesQty - page + 1 );
				return;
			}

			page++;
			job.increment();
		}
	}

	private void filterDownloadedPhotosByCategories( final List<RemotePhotoSitePhoto> remotePhotoSitePagePhotos ) {
		CollectionUtils.filter( remotePhotoSitePagePhotos, new Predicate<RemotePhotoSitePhoto>() {
			@Override
			public boolean evaluate( final RemotePhotoSitePhoto remotePhotoSitePhoto ) {
				return importParameters.getRemotePhotoSiteCategories().contains( remotePhotoSitePhoto.getRemotePhotoSiteCategory() );
			}
		} );
	}

	private List<RemotePhotoSitePhotoDiskEntry> getRemotePhotoSitePhotoDiskEntries( final RemotePhotoSiteUser remotePhotoSiteUser, final List<RemotePhotoSitePhoto> remotePhotoSitePhotos, final List<RemotePhotoSitePhoto> cachedLocallyRemotePhotoSitePhotos ) throws IOException {
		final List<RemotePhotoSitePhotoDiskEntry> result = newArrayList();
		final List<RemotePhotoSitePhotoDiskEntry> notCachedRemotePhotoSitePhotoDiskEntries = getNotCachedRemotePhotoSitePhotoDiskEntries( remotePhotoSiteUser, cachedLocallyRemotePhotoSitePhotos, remotePhotoSitePhotos );
		final List<RemotePhotoSitePhotoDiskEntry> cachedRemotePhotoSitePhotoDiskEntries = getCachedRemotePhotoSitePhotoDiskEntries( remotePhotoSitePhotos );

		result.addAll( notCachedRemotePhotoSitePhotoDiskEntries );
		result.addAll( cachedRemotePhotoSitePhotoDiskEntries );

		return result;
	}

	private RemotePhotoSitePhotosFromPageToImport getRemotePhotoSitePhotosToImport( final RemotePhotoSiteUser remotePhotoSiteUser, final List<Integer> remotePhotoSitePhotosIds, final List<RemotePhotoSitePhoto> cachedLocallyRemotePhotoSitePhotos, final User user ) throws IOException {
		final JobHelperService jobHelperService = getServices().getJobHelperService();
		final List<RemotePhotoSitePhoto> remotePhotoSitePagePhotosToImport = newArrayList();

		for ( final int remotePhotoSitePhotoId : remotePhotoSitePhotosIds ) {

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			final String remotePhotoSiteUserPageLink = importParameters.getRemoteContentHelper().getUserCardLink( remotePhotoSiteUser );

			if ( jobHelperService.doesUserPhotoExist( user.getId(), remotePhotoSitePhotoId ) ) {

				if ( importParameters.isBreakImportIfAlreadyImportedPhotoFound() ) {
					final TranslatableMessage message1 = new TranslatableMessage( "Already imported photo #$1 found. Skipping the import of the rest photos of $2", getServices() )
						.addIntegerParameter( remotePhotoSitePhotoId )
						.string( remotePhotoSiteUserPageLink )
						;

					job.addJobRuntimeLogMessage( message1 );

					return new RemotePhotoSitePhotosFromPageToImport( remotePhotoSitePagePhotosToImport, true );
				}

				log.debug( String.format( "Photo %d of %s has already been imported"
					, remotePhotoSitePhotoId
					, remotePhotoSiteUserPageLink
				) );

				final TranslatableMessage translatableMessage = new TranslatableMessage( "Photo $1 of $2 has already been imported", services )
					.addIntegerParameter( remotePhotoSitePhotoId )
					.string( remotePhotoSiteUserPageLink )
					;
				job.addJobRuntimeLogMessage( translatableMessage );

				continue;
			}

			final RemotePhotoSitePhoto remotePhotoSitePhoto;

			final RemotePhotoSitePhoto cachedRemotePhotoSitePhoto = getCachedRemotePhotoSitePhotos( remotePhotoSitePhotoId, cachedLocallyRemotePhotoSitePhotos );
			if ( cachedRemotePhotoSitePhoto != null ) {
				remotePhotoSitePhoto = cachedRemotePhotoSitePhoto;
				log.debug( String.format( "Photo %d of %s has been found in the local cache.", remotePhotoSitePhotoId, remotePhotoSiteUserPageLink ) );

				final TranslatableMessage translatableMessage = new TranslatableMessage( "Found in the local cache: $1", services )
					.string( importParameters.getRemoteContentHelper().getPhotoCardLink( remotePhotoSitePhoto ) )
					;
				job.addJobRuntimeLogMessage( translatableMessage );
			} else {
				final int delayBetweenRequest = importParameters.getDelayBetweenRequest();
				if ( delayBetweenRequest > 0 ) {
					log.debug( String.format( "Waiting %s secs", delayBetweenRequest ) );
					try {
						Thread.sleep( delayBetweenRequest * 1000 );
					} catch ( InterruptedException e ) {
						log.error( e );
					}
				}

				remotePhotoSitePhoto = makeImportPhotoFromRemotePhotoSite( remotePhotoSiteUser, remotePhotoSitePhotoId );
			}

			if ( remotePhotoSitePhoto != null ) {
				remotePhotoSitePagePhotosToImport.add( remotePhotoSitePhoto );
			}
		}

		return new RemotePhotoSitePhotosFromPageToImport( remotePhotoSitePagePhotosToImport, false );
	}

	private List<RemotePhotoSitePhotoDiskEntry> getCachedRemotePhotoSitePhotoDiskEntries( final List<RemotePhotoSitePhoto> remotePhotoSitePhotos ) throws IOException {

		final List<RemotePhotoSitePhoto> cachedRemotePhotoSitePhotos = newArrayList( remotePhotoSitePhotos );
		CollectionUtils.filter( cachedRemotePhotoSitePhotos, new Predicate<RemotePhotoSitePhoto>() {
			@Override
			public boolean evaluate( final RemotePhotoSitePhoto remotePhotoSitePhoto ) {
				return remotePhotoSitePhoto.isCached();
			}
		} );

		final List<RemotePhotoSitePhotoDiskEntry> result = newArrayList();

		for ( final RemotePhotoSitePhoto remotePhotoSitePhoto : cachedRemotePhotoSitePhotos ) {
			final File imageFile = getRemotePhotoSitePhotoImageFileUtils().getRemoteSitePhotoLocalImageFile( remotePhotoSitePhoto );

			if ( ! imageFile.exists() ) {
				continue;
			}

			final ImageDiscEntry imageDiscEntry = new ImageDiscEntry( imageFile, RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( remotePhotoSitePhoto.getRemotePhotoSiteCategory() ) );
			result.add( new RemotePhotoSitePhotoDiskEntry( remotePhotoSitePhoto, imageDiscEntry ) );
		}

		return result;
	}

	private List<RemotePhotoSitePhotoDiskEntry> getNotCachedRemotePhotoSitePhotoDiskEntries( final RemotePhotoSiteUser remotePhotoSiteUser, final List<RemotePhotoSitePhoto> cachedLocallyRemotePhotoSitePhotos, final List<RemotePhotoSitePhoto> remotePhotoSitePhotos ) throws IOException {

		final List<RemotePhotoSitePhoto> notCachedRemotePhotoSitePhotos = newArrayList( remotePhotoSitePhotos );
		CollectionUtils.filter( notCachedRemotePhotoSitePhotos, new Predicate<RemotePhotoSitePhoto>() {
			@Override
			public boolean evaluate( final RemotePhotoSitePhoto remotePhotoSitePhoto ) {
				return !remotePhotoSitePhoto.isCached();
			}
		} );

		getRemotePhotoSitePhotoImageFileUtils().prepareUserGenreFolders( remotePhotoSiteUser, notCachedRemotePhotoSitePhotos );
		final List<RemotePhotoSitePhotoDiskEntry> notCachedRemotePhotoSitePhotosDiskEntries = downloadRemotePhotoSitePhotoAndCreateDiskEntry( notCachedRemotePhotoSitePhotos );
		addRemotePhotoSitePhotosToTheLocalStorage( remotePhotoSiteUser, notCachedRemotePhotoSitePhotos, cachedLocallyRemotePhotoSitePhotos );

		return notCachedRemotePhotoSitePhotosDiskEntries;
	}

	private RemotePhotoSitePhoto getCachedRemotePhotoSitePhotos( final int remotePhotoSitePhotoId, final List<RemotePhotoSitePhoto> cachedLocallyRemotePhotoSitePhotos ) {
		for ( final RemotePhotoSitePhoto cachedLocallyRemotePhotoSitePhoto : cachedLocallyRemotePhotoSitePhotos ) {
			if ( cachedLocallyRemotePhotoSitePhoto.getPhotoId() == remotePhotoSitePhotoId ) {
				return cachedLocallyRemotePhotoSitePhoto;
			}
		}
		return null;
	}

	private String getUserName( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {

		final String remotePhotoSiteUserName = importParameters.getRemoteContentHelper().extractUserNameFromRemoteSite( remotePhotoSiteUser );

		if ( StringUtils.isEmpty( remotePhotoSiteUserName ) ) {
			final String message = String.format( "Can not extract a name of a remote photo site user #%s from a page content. Photos import of the user will be skipped.", importParameters.getRemoteContentHelper().getUserCardLink( remotePhotoSiteUser ) );
			log.error( message );

			final TranslatableMessage translatableMessage = new TranslatableMessage( "Can not extract a name of a remote photo site user #$1 from page content. Photos import of the user will be skipped.", services )
				.string( importParameters.getRemoteContentHelper().getUserCardLink( remotePhotoSiteUser ) )
				;
			job.addJobRuntimeLogMessage( translatableMessage );
		}

		return remotePhotoSiteUserName;
	}

	private void prepareFolderForImageDownloading( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {
		getRemotePhotoSitePhotoImageFileUtils().createUserFolderForPhotoDownloading( remotePhotoSiteUser );
		getRemotePhotoSiteCacheXmlUtils().createUserInfoFile( remotePhotoSiteUser );
	}

	private void addRemotePhotoSitePhotosToTheLocalStorage( final RemotePhotoSiteUser remotePhotoSiteUser, final List<RemotePhotoSitePhoto> remotePhotoSitePhotos, final List<RemotePhotoSitePhoto> cachedLocallyRemotePhotoSitePhotos ) throws IOException {

		cachedLocallyRemotePhotoSitePhotos.addAll( remotePhotoSitePhotos );

		getRemotePhotoSiteCacheXmlUtils().cachedLocallyPhotos( remotePhotoSiteUser, cachedLocallyRemotePhotoSitePhotos, services.getDateUtilsService() );
	}

	private List<RemotePhotoSitePhoto> getCachedLocallyRemotePhotoSitePhotos( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {

		final RemotePhotoSiteCacheXmlUtils remotePhotoSiteCacheXmlUtils = getRemotePhotoSiteCacheXmlUtils();

		try {
			return remotePhotoSiteCacheXmlUtils.getPhotosFromRemoteSiteUserInfoFile( remotePhotoSiteUser, services, job.getJobEnvironment().getLanguage() );
		} catch ( DocumentException e ) {
			final TranslatableMessage translatableMessage = new TranslatableMessage( "Error reading user info file: $1<br />$2", services )
				.string( remotePhotoSiteCacheXmlUtils.getUserInfoFile( remotePhotoSiteUser ).getAbsolutePath() )
				.string( e.getMessage() )
				;
			job.addJobRuntimeLogMessage( translatableMessage );

			log.error( String.format( "Error reading user info file: %s<br />", remotePhotoSiteCacheXmlUtils.getUserInfoFile( remotePhotoSiteUser ).getAbsolutePath() ), e );

			throw new BaseRuntimeException( e );
		}
	}

	private List<RemotePhotoSitePhotoDiskEntry> downloadRemotePhotoSitePhotoAndCreateDiskEntry( final List<RemotePhotoSitePhoto> remotePhotoSitePhotos ) throws IOException {

		final List<RemotePhotoSitePhotoDiskEntry> result = newArrayList();

		for ( final RemotePhotoSitePhoto remotePhotoSitePhoto : remotePhotoSitePhotos ) {

			final String imageUrl = remotePhotoSitePhoto.getImageUrl();
			log.debug( String.format( "Getting photo %s content", imageUrl ) );

			final String imageContent = importParameters.getRemoteContentHelper().getImageContentFromUrl( imageUrl );
			if ( imageContent == null ) {
				job.addJobRuntimeLogMessage( new TranslatableMessage( "Can not get remote photo site image content: '$1'", services ).string( remotePhotoSitePhoto.getImageUrl() ) );
				continue;
			}

			final ImageDiscEntry imageDiscEntry = getRemotePhotoSitePhotoImageFileUtils().createRemotePhotoSiteDiskEntry( remotePhotoSitePhoto, imageContent );

			result.add( new RemotePhotoSitePhotoDiskEntry( remotePhotoSitePhoto, imageDiscEntry ) );
		}

		return result;
	}

	private List<RemotePhotoSiteDBEntry> preparePhotosToImport( final RemotePhotoSiteUser remotePhotoSiteUser, final User localUser, final List<RemotePhotoSitePhotoDiskEntry> remotePhotoSitePhotoDiskEntries ) throws IOException {

		final List<RemotePhotoSiteDBEntry> photosToImport = newArrayList();

		for ( final RemotePhotoSitePhotoDiskEntry remotePhotoSitePhotoDiskEntry : remotePhotoSitePhotoDiskEntries ) {

			final RemotePhotoSitePhoto remotePhotoSitePhoto = remotePhotoSitePhotoDiskEntry.getRemotePhotoSitePhoto();
			final ImageDiscEntry imageDiscEntry = remotePhotoSitePhotoDiskEntry.getImageDiscEntry();

			final ImageToImport imageToImport = new ImageToImport( imageDiscEntry );
			imageToImport.setUser( localUser );
			imageToImport.setName( remotePhotoSitePhoto.getName() );

			final RemotePhotoSiteCategory remotePhotoSiteCategory = remotePhotoSitePhoto.getRemotePhotoSiteCategory();
			final DateUtilsService dateUtilsService = services.getDateUtilsService();

			final GenreDiscEntry genreDiscEntry = RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( remotePhotoSiteCategory );
			final String siteUrl = importParameters.getRemoteContentHelper().getRemotePhotoSiteHost();
			final String description = String.format( "Imported from '%s' at %s ( %s ). Photo category: %s."
				, siteUrl
				, dateUtilsService.formatDateTime( dateUtilsService.getCurrentTime() )
				, importParameters.getRemoteContentHelper().getPhotoCardLink( remotePhotoSitePhoto )
				, genreDiscEntry.getName()
			);
			imageToImport.setPhotoDescription( description );

			final String keywords = StringUtilities.truncateString( String.format( "%s, %s, %s", siteUrl, remotePhotoSiteUser.getName(), imageToImport.getName() ), 255 );
			imageToImport.setPhotoKeywords( keywords );
			imageToImport.setUploadTime( remotePhotoSitePhoto.getUploadTime() );
			imageToImport.setImportId( remotePhotoSitePhotoDiskEntry.getRemotePhotoSitePhoto().getPhotoId() );

			photosToImport.add( new RemotePhotoSiteDBEntry( remotePhotoSitePhotoDiskEntry, imageToImport ) );
		}

		return photosToImport;
	}

	private List<Integer> extractUserPhotosIdsFromPage( final String userPageContent ) {
		final List<Integer> result = newArrayList();

		final Pattern pattern = Pattern.compile( getRemotePhotoSitePageContentDataExtractor().getPhotoIdRegex() );
		final Matcher matcher = pattern.matcher( userPageContent );

		while ( matcher.find() && ! job.hasJobFinishedWithAnyResult() ) {
			final String group = matcher.group( 1 );
			final int remotePhotoSitePhotoId = getRemotePhotoSitePageContentDataExtractor().extractRemotePhotoSitePhotoId( group );

			result.add( remotePhotoSitePhotoId );
		}
		return result;
	}

	private RemotePhotoSitePhoto makeImportPhotoFromRemotePhotoSite( final RemotePhotoSiteUser remotePhotoSiteUser, final int remotePhotoSitePhotoId ) throws IOException {

		final AbstractRemoteContentHelper remoteContentHelper = importParameters.getRemoteContentHelper();

		final String photoPageContent = remoteContentHelper.getPhotoPageContent( remotePhotoSiteUser, remotePhotoSitePhotoId );
		if ( StringUtils.isEmpty( photoPageContent ) ) {
			logPhotoSkipping( remotePhotoSiteUser, remotePhotoSitePhotoId, "Can't load photo page content." );
			return null;
		}

		final String imageUrl = getRemotePhotoSitePageContentDataExtractor().extractImageUrl( remotePhotoSitePhotoId, photoPageContent );
		if ( StringUtils.isEmpty( imageUrl ) ) {
			logPhotoSkipping( remotePhotoSiteUser, remotePhotoSitePhotoId, "Can not extract photo image URL from page content." );
			return null;
		}

		final AbstractRemotePhotoSitePageContentDataExtractor remotePhotoSitePageContentHelper = getRemotePhotoSitePageContentDataExtractor();

		final RemotePhotoSiteCategory remotePhotoSiteCategory = RemotePhotoSiteCategory.getById( remotePhotoSitePageContentHelper.extractPhotoCategoryId( photoPageContent ) );
		if( remotePhotoSiteCategory == null ) {
			logPhotoSkipping( remotePhotoSiteUser, remotePhotoSitePhotoId, "Can not extract photo category from page content." );
			return null;
		}

		final RemotePhotoSitePhoto remotePhotoSitePhoto = new RemotePhotoSitePhoto( remotePhotoSiteUser, remotePhotoSitePhotoId, remotePhotoSiteCategory );
		remotePhotoSitePhoto.setName( remotePhotoSitePageContentHelper.extractPhotoName( photoPageContent ) );

		final Date uploadTime = extractUploadTime( photoPageContent );
		if ( uploadTime != null ) {
			remotePhotoSitePhoto.setUploadTime( uploadTime );
		} else {
			remotePhotoSitePhoto.setUploadTime( services.getRandomUtilsService().getRandomDate( firstPhotoUploadTime, services.getDateUtilsService().getCurrentTime() ) );

			final TranslatableMessage translatableMessage = new TranslatableMessage( "$1: can not get upload time from remote photo page. Random time is used.", services )
				.string( remoteContentHelper.getPhotoCardLink( remotePhotoSitePhoto ) )
				;
			job.addJobRuntimeLogMessage( translatableMessage );
		}

		remotePhotoSitePhoto.setImageUrl( imageUrl );

		if ( importParameters.isImportComments() ) {
			final List<String> comments = getRemotePhotoSitePageContentDataExtractor().extractComments( photoPageContent );
			remotePhotoSitePhoto.setComments( comments );
		}

		remotePhotoSitePhoto.setCached( false );

		log.debug( String.format( "Photo %d has been downloaded from remote photo site", remotePhotoSitePhoto.getPhotoId() ) );

		final TranslatableMessage translatableMessage = new TranslatableMessage( "Downloaded from '$1': $2 of $3, photo category: $4", services )
			.string( remoteContentHelper.getRemotePhotoSiteHost() )
			.string( remoteContentHelper.getPhotoCardLink( remotePhotoSitePhoto ) )
			.string( remoteContentHelper.getUserCardLink( remotePhotoSiteUser ) )
			.string( remoteContentHelper.getPhotoCategoryLink( remotePhotoSitePhoto.getRemotePhotoSiteCategory(), services.getEntityLinkUtilsService(), services.getGenreService(), importParameters.getLanguage() ) )
			;
		job.addJobRuntimeLogMessage( translatableMessage );

		return remotePhotoSitePhoto;

	}

	private void logPhotoSkipping( final RemotePhotoSiteUser remotePhotoSiteUser, final int remotePhotoSitePhotoId, final String s ) {

		final TranslatableMessage translatableMessage = new TranslatableMessage( "$1 User: $2; photo: $3. Photo import skipped.", services )
			.string( s )
			.string( remotePhotoSiteUser.toString() ) // TODO: ?
			.string( importParameters.getRemoteContentHelper().getPhotoCardLink( remotePhotoSitePhotoId ) )
			;
		job.addJobRuntimeLogMessage( translatableMessage );

		log.warn( String.format( "%s Photo #%d. Photo import skipped.", s, remotePhotoSitePhotoId ) );
	}

	private void importComments( final List<RemotePhotoSiteDBEntry> dbEntries ) {
		for ( final RemotePhotoSiteDBEntry dbEntry : dbEntries ) {
			createPhotoComments( dbEntry );
		}
	}

	private void createPhotoComments( final RemotePhotoSiteDBEntry dbEntry ) {

		final RandomUtilsService randomUtilsService = services.getRandomUtilsService();
		final DateUtilsService dateUtilsService = services.getDateUtilsService();
		final PhotoCommentService photoCommentService = services.getPhotoCommentService();

		final RemotePhotoSitePhoto remotePhotoSitePhoto = dbEntry.getRemotePhotoSitePhotoDiskEntry().getRemotePhotoSitePhoto();
		log.debug( String.format( "Importing comments for photo %d", remotePhotoSitePhoto.getPhotoId() ) );

		final Photo photo = dbEntry.getImageToImport().getPhoto();

		final Date photoUploadTime = photo.getUploadTime();

		final Date now = dateUtilsService.getCurrentDate();

		final List<String> comments = remotePhotoSitePhoto.getComments();

		final int commentsQty = comments.size();

		if ( commentsQty == 0 ) {
			return;
		}

		final int differenceInSeconds = dateUtilsService.getDifferenceInSeconds( photoUploadTime, now );
		final int intervalBetweenCommentsInSeconds = differenceInSeconds / commentsQty;
		Date commentTime = photoUploadTime;

		for ( final String commentText : comments ) {
			final PhotoComment comment = new PhotoComment();
			comment.setPhotoId( photo.getId() );
			comment.setCommentText( commentText.replace( "<br/>", "\n" ) );
			comment.setCommentAuthor( randomUtilsService.getRandomUser( job.getBeingProcessedUsers() ) );

			commentTime = dateUtilsService.getTimeOffsetInSeconds( commentTime, intervalBetweenCommentsInSeconds );
			comment.setCreationTime( commentTime );

			photoCommentService.save( comment );
		}
	}

	private String getUserCardFileName( final RemotePhotoSiteUser remotePhotoSiteUser, final int page ) {
		return String.format( "userCard_%s_page_%d", remotePhotoSiteUser.getId(), page );
	}

	private String getPhotoCardFileName( final RemotePhotoSitePhoto remotePhotoSitePhoto ) {
		return String.format( "photoCard_%d", remotePhotoSitePhoto.getPhotoId() );
	}

	private User findByNameOrCreateUser( final RemotePhotoSiteUser remotePhotoSiteUser, final RemoteSitePhotosImportParameters parameters ) throws IOException {

		final String userName = getUserName( remotePhotoSiteUser );
		if ( StringUtils.isEmpty( userName ) ) {
			return null;
		}

		remotePhotoSiteUser.setName( userName );

		final String userLogin = createLoginForRemotePhotoSiteUser( remotePhotoSiteUser.getId() );
		final User existingUser = services.getUserService().loadByLogin( userLogin );
		if ( existingUser != null ) {

			final TranslatableMessage translatableMessage = new TranslatableMessage( "Existing user found: $1", services )
				.addUserCardLinkParameter( existingUser )
				;
			job.addJobRuntimeLogMessage( translatableMessage );

			return existingUser;
		}

		final User user = services.getFakeUserService().getRandomUser();

		user.setLogin( userLogin );
		user.setName( userName );
		user.setMembershipType( parameters.getMembershipType() );
		user.setGender( parameters.getUserGender() );
		user.setSelfDescription( String.format( "A user of %s: %s ( %s )", importParameters.getRemoteContentHelper().getRemotePhotoSiteHost(), remotePhotoSiteUser.getId(), importParameters.getRemoteContentHelper().getUserCardUrl( remotePhotoSiteUser.getId(), 1 ) ) );

		if ( ! services.getUserService().save( user ) ) {
			throw new BaseRuntimeException( "Can not create user" );
		}

		final TranslatableMessage translatableMessage = new TranslatableMessage( "New user has been created: $1", services )
			.addUserCardLinkParameter( user )
			;
		job.addJobRuntimeLogMessage( translatableMessage );

		return user;
	}

	private Date extractUploadTime( final String photoPageContent ) {
		final Pattern pattern = Pattern.compile( "\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}\\:\\d{2}" );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			final String uploadDateTime = matcher.group();

			if ( StringUtils.isEmpty( uploadDateTime )) {
				return null;
			}

			final String[] dateAndTime = uploadDateTime.split( "\\s" );

			if ( dateAndTime.length < 2) {
				return null;
			}

			return services.getDateUtilsService().parseDateTime( dateAndTime[0], dateAndTime[1], "dd.MM.yyyy HH:mm" );
		}

		return extractUploadTodayTime( photoPageContent );
	}

	private Date extractUploadTodayTime( final String photoPageContent ) {
		final Pattern pattern = Pattern.compile( "\\s\\d{2}\\:\\d{2}(\\s*?)</b>" );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			final String uploadDateTime = matcher.group();

			if ( StringUtils.isEmpty( uploadDateTime )) {
				return null;
			}

			final String time = uploadDateTime.substring( 0, uploadDateTime.length() -4 ).trim();

			final DateUtilsService dateUtilsService = services.getDateUtilsService();
			return dateUtilsService.parseDateTime( dateUtilsService.formatDate( dateUtilsService.getCurrentDate() ), String.format( "%s:00", time ) );
		}

		return null;
	}

	private class RemotePhotoSitePhotosFromPageToImport {

		private final List<RemotePhotoSitePhoto> remotePhotoSitePhotos;
		private final boolean breakImportAfterThisPageProcessed;

		private RemotePhotoSitePhotosFromPageToImport( final List<RemotePhotoSitePhoto> remotePhotoSitePhotos, final boolean breakImportAfterThisPageProcessed ) {
			this.remotePhotoSitePhotos = remotePhotoSitePhotos;
			this.breakImportAfterThisPageProcessed = breakImportAfterThisPageProcessed;
		}

		public List<RemotePhotoSitePhoto> getRemotePhotoSitePhotos() {
			return remotePhotoSitePhotos;
		}

		public boolean isBreakImportAfterThisPageProcessed() {
			return breakImportAfterThisPageProcessed;
		}
	}

	private RemotePhotoSitePhotoImageFileUtils getRemotePhotoSitePhotoImageFileUtils() {
		return new RemotePhotoSitePhotoImageFileUtils( importParameters.getRemoteContentHelper().getPhotosImportSource(), services.getSystemVarsService().getRemotePhotoSitesCacheFolder() );
	}

	private RemotePhotoSiteCacheXmlUtils getRemotePhotoSiteCacheXmlUtils() {
		return new RemotePhotoSiteCacheXmlUtils( importParameters.getRemoteContentHelper().getPhotosImportSource(), services.getSystemVarsService().getRemotePhotoSitesCacheFolder() );
	}

	private AbstractRemotePhotoSitePageContentDataExtractor getRemotePhotoSitePageContentDataExtractor() {
		return importParameters.getRemotePhotoSitePageContentDataExtractor();
	}
}

