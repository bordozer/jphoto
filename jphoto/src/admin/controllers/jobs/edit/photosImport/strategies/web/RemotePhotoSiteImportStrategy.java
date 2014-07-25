package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.ImageDiscEntry;
import admin.controllers.jobs.edit.photosImport.ImageToImport;
import admin.controllers.jobs.edit.photosImport.importParameters.AbstractImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.RemoteSitePhotosImportParameters;
import admin.controllers.jobs.edit.photosImport.strategies.AbstractPhotoImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.*;
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

	public static final String REMOTE_SITE_USER_LOGIN_PREFIX = "PS_";

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

		for ( final String photosightUserId : importParameters.getRemoteUserIds() ) {

			final RemotePhotoSiteUser remotePhotoSiteUser = new RemotePhotoSiteUser( photosightUserId );

			importPhotosightUserPhotos( remotePhotoSiteUser );

			if( job.hasJobFinishedWithAnyResult() ) {
				break;
			}
		}
	}

	private void importPhotosightUserPhotos( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException, SaveToDBException {

		final User user = findByNameOrCreateUser( remotePhotoSiteUser, importParameters );
		if ( user == null ) {
			log.error( String.format( "%s can not be created. Skipping user's photos import.", remotePhotoSiteUser ) );
			return;
		}

		prepareFolderForImageDownloading( remotePhotoSiteUser );

		final List<RemotePhotoSitePhoto> cachedLocallyRemotePhotoSitePhotos = getCachedLocallyPhotosightPhotos( remotePhotoSiteUser );

		int page = 1;

		final String userFirstPageContent = importParameters.getRemoteContentHelper().getUserPageContent( 1, remotePhotoSiteUser.getId() );
		if ( StringUtils.isEmpty( userFirstPageContent ) ) {
			return; // can not load page - just skipping
		}

		final int userPagesQty = PhotosightContentHelper.getTotalPagesQty( userFirstPageContent, remotePhotoSiteUser.getId() );

		while ( ! job.isFinished() && ! job.hasJobFinishedWithAnyResult() && page <= userPagesQty ) {

			log.debug( String.format( "Getting page %d context of %s", page, importParameters.getRemoteContentHelper().getRemotePhotoSiteUserPageLink( remotePhotoSiteUser ) ) );

			final String userPageContent = importParameters.getRemoteContentHelper().getUserPageContent( page, remotePhotoSiteUser.getId() );
			if ( StringUtils.isEmpty( userPageContent ) ) {

				log.info( "Can not load photosight user first page - skipping import user's photos" );

				continue;
			}

			final List<Integer> photosightPagePhotosIds = extractUserPhotosIdsFromPage( userPageContent );

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			if ( photosightPagePhotosIds.size() == 0 ) {
				final String userCardFileName = getUserCardFileName( remotePhotoSiteUser, page );
				final File file = PhotosightImageFileUtils.writePageContentToFile( userCardFileName, userPageContent );

				final TranslatableMessage translatableMessage = new TranslatableMessage( "No photo have been found on page $1. User page content saved. See $2", services )
					.addIntegerParameter( page )
					.string( file.getAbsolutePath() )
					;
				job.addJobRuntimeLogMessage( translatableMessage );

				log.info( String.format( "No photo have been found on page %d. User page content saved. See %s", page, file.getCanonicalPath() ) );

				continue;
			}

			final PhotosightPhotosFromPageToImport photosightPhotosToImport = getPhotosightPhotosToImport( remotePhotoSiteUser, photosightPagePhotosIds, cachedLocallyRemotePhotoSitePhotos, user );
			final List<RemotePhotoSitePhoto> photosightPagePhotos = photosightPhotosToImport.getRemotePhotoSitePhotos();

			filterDownloadedPhotosByPhotosightCategories( photosightPagePhotos );

			final List<PhotosightPhotoOnDisk> photosightPhotosOnDisk = getPhotosightPhotoOnDisk( remotePhotoSiteUser, photosightPagePhotos, cachedLocallyRemotePhotoSitePhotos );

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			final List<PhotosightDBEntry> entries = prepareImagesToImport( remotePhotoSiteUser, user, photosightPhotosOnDisk );

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			final List<ImageToImport> imagesToImport = newArrayList();
			for ( final PhotosightDBEntry dbEntry : entries ) {
				imagesToImport.add( dbEntry.getImageToImport() );
			}

			createPhotosDBEntries( imagesToImport );

			if ( importParameters.isImportComments() ) {
				importComments( entries );
			}

			if ( photosightPhotosToImport.isBreakImportAfterThisPageProcessed() ) {
				job.increment( userPagesQty - page + 1 );
				return;
			}

			page++;
			job.increment();
		}
	}

	private void filterDownloadedPhotosByPhotosightCategories( final List<RemotePhotoSitePhoto> photosightPagePhotos ) {
		CollectionUtils.filter( photosightPagePhotos, new Predicate<RemotePhotoSitePhoto>() {
			@Override
			public boolean evaluate( final RemotePhotoSitePhoto remotePhotoSitePhoto ) {
				return importParameters.getRemotePhotoSiteCategories().contains( remotePhotoSitePhoto.getRemotePhotoSiteCategory() );
			}
		} );
	}

	private List<PhotosightPhotoOnDisk> getPhotosightPhotoOnDisk( final RemotePhotoSiteUser remotePhotoSiteUser, final List<RemotePhotoSitePhoto> photosightPagePhotos, final List<RemotePhotoSitePhoto> cachedLocallyRemotePhotoSitePhotos ) throws IOException {
		final List<PhotosightPhotoOnDisk> photosightPhotosOnDisk = newArrayList();
		final List<PhotosightPhotoOnDisk> notCachedPhotosightPhotosOnDisk = getNotCachedPhotosightPhotosOnDisk( remotePhotoSiteUser, cachedLocallyRemotePhotoSitePhotos, photosightPagePhotos );
		final List<PhotosightPhotoOnDisk> cachedPhotosightPhotosOnDisk = getCachedPhotosightPhotosOnDisk( photosightPagePhotos );

		photosightPhotosOnDisk.addAll( notCachedPhotosightPhotosOnDisk );
		photosightPhotosOnDisk.addAll( cachedPhotosightPhotosOnDisk );
		return photosightPhotosOnDisk;
	}

	private PhotosightPhotosFromPageToImport getPhotosightPhotosToImport( final RemotePhotoSiteUser remotePhotoSiteUser, final List<Integer> photosightPagePhotosIds, final List<RemotePhotoSitePhoto> cachedLocallyRemotePhotoSitePhotos, final User user ) throws IOException {
		final JobHelperService jobHelperService = getServices().getJobHelperService();
		final List<RemotePhotoSitePhoto> photosightPagePhotos = newArrayList();

		for ( final int photosightPhotoId : photosightPagePhotosIds ) {

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			final String photosightUserPageLink = importParameters.getRemoteContentHelper().getRemotePhotoSiteUserPageLink( remotePhotoSiteUser );

			if ( jobHelperService.doesUserPhotoExist( user.getId(), photosightPhotoId ) ) {

				if ( importParameters.isBreakImportIfAlreadyImportedPhotoFound() ) {
					final TranslatableMessage message1 = new TranslatableMessage( "Already imported photo #$1 found. Skipping the import of the rest photos of $2", getServices() )
						.addIntegerParameter( photosightPhotoId )
						.string( photosightUserPageLink )
						;

					job.addJobRuntimeLogMessage( message1 );

					return new PhotosightPhotosFromPageToImport( photosightPagePhotos, true );
				}

				log.debug( String.format( "Photo %d of %s has already been imported"
					, photosightPhotoId
					, photosightUserPageLink
				) );

				final TranslatableMessage translatableMessage = new TranslatableMessage( "Photo $1 of $2 has already been imported", services )
					.addIntegerParameter( photosightPhotoId )
					.string( photosightUserPageLink )
					;
				job.addJobRuntimeLogMessage( translatableMessage );

				continue;
			}

			final RemotePhotoSitePhoto remotePhotoSitePhoto;

			final RemotePhotoSitePhoto cachedRemotePhotoSitePhoto = getPhotosightPhotoFromCache( photosightPhotoId, cachedLocallyRemotePhotoSitePhotos );
			if ( cachedRemotePhotoSitePhoto != null ) {
				remotePhotoSitePhoto = cachedRemotePhotoSitePhoto;
				log.debug( String.format( "Photo %d of %s has been found in the local cache.", photosightPhotoId, photosightUserPageLink ) );

				final TranslatableMessage translatableMessage = new TranslatableMessage( "Found in the local cache: $1", services )
					.string( importParameters.getRemoteContentHelper().getRemotePhotoSitePhotoPageLink( remotePhotoSitePhoto ) )
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

				remotePhotoSitePhoto = importPhotoFromPhotosight( remotePhotoSiteUser, photosightPhotoId );
			}

			if ( remotePhotoSitePhoto != null ) {
				photosightPagePhotos.add( remotePhotoSitePhoto );
			}
		}

		return new PhotosightPhotosFromPageToImport( photosightPagePhotos, false );
	}

	private List<PhotosightPhotoOnDisk> getCachedPhotosightPhotosOnDisk( final List<RemotePhotoSitePhoto> photosightPagePhotos ) throws IOException {
		final List<RemotePhotoSitePhoto> cachedPhotosightPagePhoto = newArrayList( photosightPagePhotos );
		CollectionUtils.filter( cachedPhotosightPagePhoto, new Predicate<RemotePhotoSitePhoto>() {
			@Override
			public boolean evaluate( final RemotePhotoSitePhoto remotePhotoSitePhoto ) {
				return remotePhotoSitePhoto.isCached();
			}
		} );

		final List<PhotosightPhotoOnDisk> result = newArrayList();

		for ( final RemotePhotoSitePhoto remotePhotoSitePhoto : cachedPhotosightPagePhoto ) {
			final File imageFile = PhotosightImageFileUtils.getPhotosightPhotoLocalImageFile( remotePhotoSitePhoto );

			if ( ! imageFile.exists() ) {
				continue;
			}

			final ImageDiscEntry imageDiscEntry = new ImageDiscEntry( imageFile, PhotosightImageFileUtils.getGenreDiscEntry( remotePhotoSitePhoto.getRemotePhotoSiteCategory() ) );
			result.add( new PhotosightPhotoOnDisk( remotePhotoSitePhoto, imageDiscEntry ) );
		}

		return result;
	}

	private List<PhotosightPhotoOnDisk> getNotCachedPhotosightPhotosOnDisk( final RemotePhotoSiteUser remotePhotoSiteUser, final List<RemotePhotoSitePhoto> cachedLocallyRemotePhotoSitePhotos, final List<RemotePhotoSitePhoto> photosightPagePhotos ) throws IOException {
		final List<RemotePhotoSitePhoto> notCachedPhotosightPagePhoto = newArrayList( photosightPagePhotos );
		CollectionUtils.filter( notCachedPhotosightPagePhoto, new Predicate<RemotePhotoSitePhoto>() {
			@Override
			public boolean evaluate( final RemotePhotoSitePhoto remotePhotoSitePhoto ) {
				return !remotePhotoSitePhoto.isCached();
			}
		} );

		PhotosightImageFileUtils.prepareUserGenreFolders( remotePhotoSiteUser, notCachedPhotosightPagePhoto );
		final List<PhotosightPhotoOnDisk> notCachedPhotosightPhotosOnDisk = downloadPhotosightPhotosOnDisk( notCachedPhotosightPagePhoto );
		cachePagePhotosightPhotosLocally( remotePhotoSiteUser, notCachedPhotosightPagePhoto, cachedLocallyRemotePhotoSitePhotos );
		return notCachedPhotosightPhotosOnDisk;
	}

	private RemotePhotoSitePhoto getPhotosightPhotoFromCache( final int photosightPhotoId, final List<RemotePhotoSitePhoto> cachedLocallyRemotePhotoSitePhotos ) {
		for ( final RemotePhotoSitePhoto cachedLocallyRemotePhotoSitePhoto : cachedLocallyRemotePhotoSitePhotos ) {
			if ( cachedLocallyRemotePhotoSitePhoto.getPhotoId() == photosightPhotoId ) {
				return cachedLocallyRemotePhotoSitePhoto;
			}
		}
		return null;
	}

	private String getUserName( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {

		final String photosightUserName = importParameters.getRemoteContentHelper().getRemotePhotoSiteUserName( remotePhotoSiteUser );

		if ( StringUtils.isEmpty( photosightUserName ) ) {
			final String message = String.format( "Can not extract a name photosight user #%s from page content. Photos import of the user will be skipped.", importParameters.getRemoteContentHelper().getRemotePhotoSiteUserPageLink( remotePhotoSiteUser ) );
			log.error( message );

			final TranslatableMessage translatableMessage = new TranslatableMessage( "Can not extract a name of photosight user #$1 from page content. Photos import of the user will be skipped.", services )
				.string( importParameters.getRemoteContentHelper().getRemotePhotoSiteUserPageLink( remotePhotoSiteUser ) )
				;
			job.addJobRuntimeLogMessage( translatableMessage );
		}

		return photosightUserName;
	}

	private void prepareFolderForImageDownloading( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {
		PhotosightImageFileUtils.createUserFolderForPhotoDownloading( remotePhotoSiteUser );
		PhotosightXmlUtils.createUserInfoFile( remotePhotoSiteUser );
	}

	private void cachePagePhotosightPhotosLocally( final RemotePhotoSiteUser remotePhotoSiteUser, final List<RemotePhotoSitePhoto> photosightPagePhotos, final List<RemotePhotoSitePhoto> cachedLocallyRemotePhotoSitePhotos ) throws IOException {

		cachedLocallyRemotePhotoSitePhotos.addAll( photosightPagePhotos );

		PhotosightXmlUtils.cachedLocallyPhotos( remotePhotoSiteUser, cachedLocallyRemotePhotoSitePhotos, services.getDateUtilsService() );
	}

	private List<RemotePhotoSitePhoto> getCachedLocallyPhotosightPhotos( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {
		try {
			return PhotosightXmlUtils.getPhotosFromRemoteSiteUserInfoFile( remotePhotoSiteUser, services, job.getJobEnvironment().getLanguage() );
		} catch ( DocumentException e ) {
			final TranslatableMessage translatableMessage = new TranslatableMessage( "Error reading user info file: $1<br />$2", services )
				.string( PhotosightXmlUtils.getUserInfoFile( remotePhotoSiteUser ).getAbsolutePath() )
				.string( e.getMessage() )
				;
			job.addJobRuntimeLogMessage( translatableMessage );

			log.error( String.format( "Error reading user info file: %s<br />", PhotosightXmlUtils.getUserInfoFile( remotePhotoSiteUser ).getAbsolutePath() ), e );

			throw new BaseRuntimeException( e );
		}
	}

	private List<PhotosightPhotoOnDisk> downloadPhotosightPhotosOnDisk( final List<RemotePhotoSitePhoto> remotePhotoSitePhotos ) throws IOException {

		final List<PhotosightPhotoOnDisk> result = newArrayList();

		for ( final RemotePhotoSitePhoto remotePhotoSitePhoto : remotePhotoSitePhotos ) {

			final String imageUrl = remotePhotoSitePhoto.getImageUrl();
			log.debug( String.format( "Getting photo %s content", imageUrl ) );

			final String imageContent = importParameters.getRemoteContentHelper().getImageContentFromUrl( imageUrl );
			final ImageDiscEntry imageDiscEntry = PhotosightImageFileUtils.downloadPhotoOnDisk( remotePhotoSitePhoto, imageContent );
			if ( imageDiscEntry == null ) {
				job.addJobRuntimeLogMessage( new TranslatableMessage( "Can not get photosight photo image content: '$1'", services ).string( remotePhotoSitePhoto.getImageUrl() ) );
				continue;
			}
			result.add( new PhotosightPhotoOnDisk( remotePhotoSitePhoto, imageDiscEntry ) );
		}

		return result;
	}

	private List<PhotosightDBEntry> prepareImagesToImport( final RemotePhotoSiteUser remotePhotoSiteUser, final User user, final List<PhotosightPhotoOnDisk> photosightPhotosOnDisk ) throws IOException {
		final List<PhotosightDBEntry> imagesToImport = newArrayList();

		for ( final PhotosightPhotoOnDisk photosightPhotoOnDisk : photosightPhotosOnDisk ) {

			final RemotePhotoSitePhoto remotePhotoSitePhoto = photosightPhotoOnDisk.getRemotePhotoSitePhoto();
			final ImageDiscEntry imageDiscEntry = photosightPhotoOnDisk.getImageDiscEntry();

			final ImageToImport imageToImport = new ImageToImport( imageDiscEntry );
			imageToImport.setUser( user );
			imageToImport.setName( remotePhotoSitePhoto.getName() );

			final RemotePhotoSiteCategory remotePhotoSiteCategory = remotePhotoSitePhoto.getRemotePhotoSiteCategory();
			final DateUtilsService dateUtilsService = services.getDateUtilsService();

			final GenreDiscEntry genreDiscEntry = PhotosightImageFileUtils.getGenreDiscEntry( remotePhotoSiteCategory );
			final String description = String.format( "Imported from photosight at %s ( %s ). Photo category: %s."
				, dateUtilsService.formatDateTime( dateUtilsService.getCurrentTime() ), importParameters.getRemoteContentHelper().getRemotePhotoSitePhotoPageLink( remotePhotoSitePhoto ), genreDiscEntry.getName()
			);
			imageToImport.setPhotoDescription( description );

			final String keywords = StringUtilities.truncateString( String.format( "Photosight, %s, %s", remotePhotoSiteUser.getName(), imageToImport.getName() ), 255 );
			imageToImport.setPhotoKeywords( keywords );
			imageToImport.setUploadTime( remotePhotoSitePhoto.getUploadTime() );
			imageToImport.setImportId( photosightPhotoOnDisk.getRemotePhotoSitePhoto().getPhotoId() );

//			PhotosightImportStrategy.writeToFile( PhotosightImportStrategy.getPhotoCardFileName( photosightPhoto ), photoPageContent );

			imagesToImport.add( new PhotosightDBEntry( photosightPhotoOnDisk, imageToImport ) );
		}
		return imagesToImport;
	}

	@Override
	public int getTotalOperations( final int totalJopOperations ) throws IOException {

		if ( totalJopOperations > 0 && totalJopOperations != AbstractJob.OPERATION_COUNT_UNKNOWN ) {
			return totalJopOperations;
		}

		int countedTotal = 0;

		final List<String> photosightUserIds = importParameters.getRemoteUserIds();
		for ( final String photosightUserId : photosightUserIds ) {

			final String userPageContent = importParameters.getRemoteContentHelper().getUserPageContent( 1, photosightUserId );

			if ( StringUtils.isEmpty( userPageContent ) ) {
				log.error( String.format( "ERROR getting photosight user #%s pages qty. Photos import of the user will be skipped.", photosightUserId ) );

				final TranslatableMessage translatableMessage = new TranslatableMessage( "ERROR getting photosight user #$1 pages qty. Photos import of the user will be skipped.", services )
					.string( photosightUserId )
					;
				job.addJobRuntimeLogMessage( translatableMessage );

				continue;
			}

			final int qty = PhotosightContentHelper.getTotalPagesQty( userPageContent, photosightUserId );
			countedTotal += qty;
			log.info( String.format( "Getting photosight user #%s pages qty: %d ( summary: %d )", photosightUserId, qty, countedTotal ) );

			if ( job.getGenerationMonitor().getStatus().isNotActive() ) {
				break;
			}
		}

		return countedTotal;
	}

	public static String getPhotosightUserLogin( final String photosightUserId ) {
		return String.format( "%s%s", REMOTE_SITE_USER_LOGIN_PREFIX, photosightUserId );
	}

	private List<Integer> extractUserPhotosIdsFromPage( final String userPageContent ) {
		final List<Integer> result = newArrayList();

		final Pattern pattern = Pattern.compile( PhotosightContentDataExtractor.getPhotoIdRegex() );
		final Matcher matcher = pattern.matcher( userPageContent );

		while ( matcher.find() && ! job.hasJobFinishedWithAnyResult() ) {
			final String group = matcher.group( 1 );
			final int photosightPhotoId = PhotosightContentDataExtractor.extractPhotosightPhotoId( group );

			result.add( photosightPhotoId );
		}
		return result;
	}

	private RemotePhotoSitePhoto importPhotoFromPhotosight( final RemotePhotoSiteUser remotePhotoSiteUser, final int photosightPhotoId ) throws IOException {

		final String photoPageContent = importParameters.getRemoteContentHelper().getPhotoPageContent( remotePhotoSiteUser, photosightPhotoId );
		if ( StringUtils.isEmpty( photoPageContent ) ) {
			logPhotoSkipping( remotePhotoSiteUser, photosightPhotoId, "Can't load photo page content." );
			return null;
		}

		final String imageUrl = PhotosightContentDataExtractor.extractImageUrl( photosightPhotoId, photoPageContent );
		if ( StringUtils.isEmpty( imageUrl ) ) {
			logPhotoSkipping( remotePhotoSiteUser, photosightPhotoId, "Can not extract photo image URL from page content." );
			return null;
		}

		final RemotePhotoSiteCategory remotePhotoSiteCategory = RemotePhotoSiteCategory.getById( PhotosightContentHelper.extractPhotoCategoryId( photoPageContent ) );
		if( remotePhotoSiteCategory == null ) {
			logPhotoSkipping( remotePhotoSiteUser, photosightPhotoId, "Can not extract photosight category from page content." );
			return null;
		}

		final RemotePhotoSitePhoto remotePhotoSitePhoto = new RemotePhotoSitePhoto( remotePhotoSiteUser, photosightPhotoId, remotePhotoSiteCategory );
		remotePhotoSitePhoto.setName( PhotosightContentHelper.extractPhotoName( photoPageContent ) );

		final Date uploadTime = extractUploadTime( photoPageContent );
		if ( uploadTime != null ) {
			remotePhotoSitePhoto.setUploadTime( uploadTime );
		} else {
			remotePhotoSitePhoto.setUploadTime( services.getRandomUtilsService().getRandomDate( firstPhotoUploadTime, services.getDateUtilsService().getCurrentTime() ) );

			final TranslatableMessage translatableMessage = new TranslatableMessage( "$1: can not get upload time from photosight photo page. Random time is used.", services )
				.string( importParameters.getRemoteContentHelper().getRemotePhotoSitePhotoPageLink( remotePhotoSitePhoto ) )
				;
			job.addJobRuntimeLogMessage( translatableMessage );
		}

		remotePhotoSitePhoto.setImageUrl( imageUrl );

		if ( importParameters.isImportComments() ) {
			final List<String> comments = PhotosightContentHelper.extractComments( photoPageContent );
			remotePhotoSitePhoto.setComments( comments );
		}

		remotePhotoSitePhoto.setCached( false );

		log.debug( String.format( "Photo %d has been downloaded from photosight", remotePhotoSitePhoto.getPhotoId() ) );

		final TranslatableMessage translatableMessage = new TranslatableMessage( "Downloaded from photosight: $1 of $2, photosight category: $3", services )
			.string( importParameters.getRemoteContentHelper().getRemotePhotoSitePhotoPageLink( remotePhotoSitePhoto ) )
			.string( importParameters.getRemoteContentHelper().getRemotePhotoSiteUserPageLink( remotePhotoSiteUser ) )
			.string( importParameters.getRemoteContentHelper().getRemotePhotoSiteCategoryPageLink( remotePhotoSitePhoto.getRemotePhotoSiteCategory(), services.getEntityLinkUtilsService(), services.getGenreService(), importParameters.getLanguage() ) )
			;
		job.addJobRuntimeLogMessage( translatableMessage );

		return remotePhotoSitePhoto;

	}

	private void logPhotoSkipping( final RemotePhotoSiteUser remotePhotoSiteUser, final int photosightPhotoId, final String s ) {

		final TranslatableMessage translatableMessage = new TranslatableMessage( "$1 User: $2; photo: $3. Photo import skipped.", services )
			.string( s )
			.string( remotePhotoSiteUser.toString() ) // TODO: ?
			.string( importParameters.getRemoteContentHelper().getPhotoCardLink( photosightPhotoId ) )
			;
		job.addJobRuntimeLogMessage( translatableMessage );

		log.warn( String.format( "%s Photo #%d. Photo import skipped.", s, photosightPhotoId ) );
	}

	private void importComments( final List<PhotosightDBEntry> dbEntries ) {
		for ( final PhotosightDBEntry dbEntry : dbEntries ) {
			createPhotoComments( dbEntry );
		}
	}

	private void createPhotoComments( final PhotosightDBEntry dbEntry ) {

		final RandomUtilsService randomUtilsService = services.getRandomUtilsService();
		final DateUtilsService dateUtilsService = services.getDateUtilsService();
		final PhotoCommentService photoCommentService = services.getPhotoCommentService();

		final RemotePhotoSitePhoto remotePhotoSitePhoto = dbEntry.getPhotosightPhotoOnDisk().getRemotePhotoSitePhoto();
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

		final String userLogin = getPhotosightUserLogin( remotePhotoSiteUser.getId() );
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
		user.setSelfDescription( String.format( "Photosight user: %s ( %s )", remotePhotoSiteUser.getId(), importParameters.getRemoteContentHelper().getUserCardUrl( remotePhotoSiteUser.getId(), 1 ) ) );

		if ( ! services.getUserService().save( user ) ) {
			throw new BaseRuntimeException( "Can not create user" );
		}

		/*final String translate = services.getTranslatorService().translate( "New user has been created: $1"
			, importParameters.getLanguage(), entityLinkUtilsService.getUserCardLink( user, language ) );*/
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

	private class PhotosightPhotosFromPageToImport {

		private final List<RemotePhotoSitePhoto> remotePhotoSitePhotos;
		private final boolean breakImportAfterThisPageProcessed;

		private PhotosightPhotosFromPageToImport( final List<RemotePhotoSitePhoto> remotePhotoSitePhotos, final boolean breakImportAfterThisPageProcessed ) {
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
}

