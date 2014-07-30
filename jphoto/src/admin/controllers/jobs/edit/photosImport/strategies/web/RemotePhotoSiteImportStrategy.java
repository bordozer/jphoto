package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.*;
import admin.controllers.jobs.edit.photosImport.importParameters.AbstractImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.RemoteSitePhotosImportParameters;
import admin.controllers.jobs.edit.photosImport.strategies.AbstractPhotoImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightContentDataExtractor;
import admin.jobs.entries.AbstractJob;
import admin.services.jobs.JobHelperService;
import core.exceptions.BaseRuntimeException;
import core.exceptions.SaveToDBException;
import core.general.genre.Genre;
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
import utils.NumberUtils;
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

	private final Date firstPhotoUploadTime;

	private final AbstractRemotePhotoSitePageContentDataExtractor remotePhotoSitePageContentDataExtractor;
	private final AbstractRemotePhotoSiteUrlHelper remoteContentHelper;
	private final RemotePhotoSiteCacheXmlUtils remotePhotoSiteCacheXmlUtils;

	final LogHelper log = new LogHelper( RemotePhotoSiteImportStrategy.class );

	private final PhotosImportLogger logger;

	public RemotePhotoSiteImportStrategy( final AbstractJob job, final AbstractImportParameters parameters, final Services services ) {
		super( job, services, new LogHelper( RemotePhotoSiteImportStrategy.class ), parameters.getLanguage() );

		logger = new PhotosImportLogger( job, services );

		importParameters = ( RemoteSitePhotosImportParameters ) parameters;

		this.remotePhotoSitePageContentDataExtractor = PhotosightContentDataExtractor.getInstance( importParameters.getImportSource() );
		this.remoteContentHelper = AbstractRemotePhotoSiteUrlHelper.getInstance( importParameters.getImportSource() );

		remotePhotoSiteCacheXmlUtils = new RemotePhotoSiteCacheXmlUtils (
			remoteContentHelper.getPhotosImportSource()
			, services.getSystemVarsService().getRemotePhotoSitesCacheFolder()
			, services.getRemotePhotoCategoryService()
		);

		firstPhotoUploadTime = getServices().getJobHelperService().getFirstPhotoUploadTime();
	}

	@Override
	public void doImport() throws IOException, SaveToDBException {

		for ( final String remoteUserId : importParameters.getRemoteUserIds() ) {

			final RemoteUser remoteUser = new RemoteUser( remoteUserId );

			importRemotePhotoSiteUserPhotos( remoteUser );

			if( job.hasJobFinishedWithAnyResult() ) {
				break;
			}
		}
	}

	private void importRemotePhotoSiteUserPhotos( final RemoteUser remoteUser ) throws IOException, SaveToDBException {

		logger.logUserImportImportStart( remoteUser );

		final User user = findExistingOrCreateUser( remoteUser, importParameters );
		if ( user == null ) {
			logger.logUserCanNotBeCreated( remoteUser );
			return;
		}

		remotePhotoSiteCacheXmlUtils.initRemoteUserCacheFileStructure( remoteUser );

		final List<RemotePhotoData> cacheUserPhotos = getCachedRemotePhotosData( remoteUser );

		int page = 1;

		int userPagesQty = getUserPagesToProcess( remoteUser );

		while ( !job.isFinished() && !job.hasJobFinishedWithAnyResult() && page <= userPagesQty ) {

			//			log.debug( String.format( "Getting page %d context of %s", page, remoteContentHelper.getUserCardLink( remoteUser ) ) );

			final String userPageContent = remoteContentHelper.getUserPageContent( page, remoteUser.getId() );
			if ( StringUtils.isEmpty( userPageContent ) ) {

				//				log.info( "Can not load remote photo site user first page - skipping import user's photos" );

				continue;
			}

			final List<Integer> photosOnPageIds = extractUserPhotosIdsFromPage( remoteUser.getId(), userPageContent );

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			if ( photosOnPageIds.size() == 0 ) {
				/*final String userCardFileName = getUserCardFileName( remotePhotoSiteUser, page );
				final File file = getRemotePhotoSitePhotoImageFileUtils().writePageContentToFile( userCardFileName, userPageContent );

				final TranslatableMessage translatableMessage = new TranslatableMessage( "No photo have been found on page $1. User page content saved. See $2", services )
					.addIntegerParameter( page )
					.string( file.getAbsolutePath() )
					;
				job.addJobRuntimeLogMessage( translatableMessage );*/

				log.info( String.format( "No photo have been found on page %d.", page ) );

				continue;
			}

			final RemotePhotoSitePhotosFromPageToImport remoteNotImportedPhotos = getRemotePhotoSitePhotosToImport( remoteUser, photosOnPageIds, cacheUserPhotos, user );
			final List<RemotePhotoData> notImportedPhotosFromPage = remoteNotImportedPhotos.getRemotePhotoDatas();

			filterOutPhotosWithWrongCategories( notImportedPhotosFromPage );

			final List<RemotePhotoSitePhotoDiskEntry> remotePhotoSitePhotoDiskEntries = getRemotePhotoSitePhotoDiskEntries( remoteUser, notImportedPhotosFromPage, cacheUserPhotos );
			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			final List<RemotePhotoSiteDBEntry> entries = preparePhotosToImport( remoteUser, user, remotePhotoSitePhotoDiskEntries );

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			final List<ImageToImportData> imagesToImport = newArrayList();
			for ( final RemotePhotoSiteDBEntry dbEntry : entries ) {
				imagesToImport.add( dbEntry.getImageToImportData() );
			}

			createPhotosDBEntries( imagesToImport );

			if ( importParameters.isImportComments() ) {
				importComments( entries );
			}

			if ( remoteNotImportedPhotos.isBreakImportAfterThisPageProcessed() ) {
				job.increment( userPagesQty - page + 1 );
				return;
			}

			page++;
			job.increment();
		}
	}

	@Override
	public int calculateTotalPagesToProcess( final int totalJopOperations ) throws IOException {

		int totalPages = 0;

		final List<String> remotePhotoSiteUsersIds = importParameters.getRemoteUserIds();
		for ( final String remotePhotoSiteUserId : remotePhotoSiteUsersIds ) {

			final String userPageContent = remoteContentHelper.getUserPageContent( 1, remotePhotoSiteUserId );

			if ( StringUtils.isEmpty( userPageContent ) ) {
				logger.logErrorGettingUserPagesCount( remotePhotoSiteUserId );

				continue;
			}

			final int userPagesCount = remotePhotoSitePageContentDataExtractor.getRemoteUserPagesCount( userPageContent, remotePhotoSiteUserId );
			totalPages += userPagesCount;

			logger.logGettingUserPagesCount( remotePhotoSiteUserId, userPagesCount, totalPages );

			if ( job.getGenerationMonitor().getStatus().isNotActive() ) {
				break;
			}
		}

		return totalPages;
	}

	private int getUserPagesToProcess( final RemoteUser remoteUser ) {

		final String userFirstPageContent = remoteContentHelper.getUserPageContent( 1, remoteUser.getId() );
		final boolean doesUserPageExist = StringUtils.isEmpty( userFirstPageContent );
		if ( doesUserPageExist ) {
			logger.logUserPageCountError( remoteUser );
			return 0;
		}

		final int totalPagesQty = remotePhotoSitePageContentDataExtractor.getRemoteUserPagesCount( userFirstPageContent, remoteUser.getId() );
		logger.logUserPageCountGotSuccessfully( remoteUser, totalPagesQty );

		return totalPagesQty;
	}

	public static String createLoginForRemotePhotoSiteUser( final String remotePhotoSiteUserId ) {
		return String.format( "%s%s", REMOTE_PHOTO_SITE_USER_LOGIN_PREFIX, remotePhotoSiteUserId );
	}

	private void filterOutPhotosWithWrongCategories( final List<RemotePhotoData> remotePhotoSitePagePhotos ) {
		CollectionUtils.filter( remotePhotoSitePagePhotos, new Predicate<RemotePhotoData>() {
			@Override
			public boolean evaluate( final RemotePhotoData remotePhotoData ) {
				return importParameters.getRemotePhotoSiteCategories().contains( remotePhotoData.getRemotePhotoSiteCategory() );
			}
		} );
	}

	private List<RemotePhotoSitePhotoDiskEntry> getRemotePhotoSitePhotoDiskEntries( final RemoteUser remoteUser, final List<RemotePhotoData> notImportedPhotos, final List<RemotePhotoData> cacheUserPhotos ) throws IOException {

		final List<RemotePhotoSitePhotoDiskEntry> result = newArrayList();
		final List<RemotePhotoSitePhotoDiskEntry> newlyCollectedEntries = getNotCachedEntries( remoteUser, notImportedPhotos );

		final List<RemotePhotoData> photosToAddToCache = getPhotos( newlyCollectedEntries );
		cacheUserPhotos.addAll( photosToAddToCache );

		remotePhotoSiteCacheXmlUtils.createPhotosCache( remoteUser, cacheUserPhotos, services.getDateUtilsService() );

		final List<RemotePhotoSitePhotoDiskEntry> cachedEarlieEntries = getCachedEntries( notImportedPhotos );

		result.addAll( newlyCollectedEntries );
		result.addAll( cachedEarlieEntries );

		return result;
	}

	private List<RemotePhotoData> getPhotos( final List<RemotePhotoSitePhotoDiskEntry> newlyCollectedEntries ) {
		final List<RemotePhotoData> photosToAddToCache = newArrayList();
		for ( final RemotePhotoSitePhotoDiskEntry newlyCollectedEntry : newlyCollectedEntries ) {
			photosToAddToCache.add( newlyCollectedEntry.getRemotePhotoData() );
		}
		return photosToAddToCache;
	}

	private List<RemotePhotoSitePhotoDiskEntry> getNotCachedEntries( final RemoteUser remoteUser, final List<RemotePhotoData> remotePhotoDatas ) throws IOException {

		final List<RemotePhotoData> notCachedRemotePhotoDatas = newArrayList( remotePhotoDatas );
		CollectionUtils.filter( notCachedRemotePhotoDatas, new Predicate<RemotePhotoData>() {
			@Override
			public boolean evaluate( final RemotePhotoData remotePhotoData ) {
				return ! remotePhotoData.isCached();
			}
		} );

		remotePhotoSiteCacheXmlUtils.prepareUserGenreFolders( remoteUser, notCachedRemotePhotoDatas );

		return downloadRemotePhotoSitePhotoAndCache( notCachedRemotePhotoDatas );
	}

	private List<RemotePhotoSitePhotoDiskEntry> getCachedEntries( final List<RemotePhotoData> remotePhotoDatas ) throws IOException {

		final List<RemotePhotoData> cachedRemotePhotoDatas = newArrayList( remotePhotoDatas );
		CollectionUtils.filter( cachedRemotePhotoDatas, new Predicate<RemotePhotoData>() {
			@Override
			public boolean evaluate( final RemotePhotoData remotePhotoData ) {
				return remotePhotoData.isCached();
			}
		} );

		final List<RemotePhotoSitePhotoDiskEntry> result = newArrayList();

		for ( final RemotePhotoData remotePhotoData : cachedRemotePhotoDatas ) {
			final File imageFile = remotePhotoSiteCacheXmlUtils.getRemotePhotoCacheFile( remotePhotoData );

			if ( ! imageFile.exists() ) {
				continue;
			}

			final ImageToImport imageToImport = new ImageToImport( importParameters.getImportSource(), remotePhotoData.getRemotePhotoSiteCategory().getKey(), imageFile );
			result.add( new RemotePhotoSitePhotoDiskEntry( remotePhotoData, imageToImport ) );
		}

		return result;
	}

	private RemotePhotoSitePhotosFromPageToImport getRemotePhotoSitePhotosToImport( final RemoteUser remoteUser, final List<Integer> remotePhotoSitePhotosIds, final List<RemotePhotoData> cachedLocallyRemotePhotoDatas, final User user ) throws IOException {

		final JobHelperService jobHelperService = getServices().getJobHelperService();

		final List<RemotePhotoData> result = newArrayList();

		final String remotePhotoSiteUserPageLink = remoteContentHelper.getUserCardLink( remoteUser );

		for ( final int remotePhotoSitePhotoId : remotePhotoSitePhotosIds ) {

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			if ( jobHelperService.doesUserPhotoExist( user.getId(), remotePhotoSitePhotoId ) ) {

				if ( importParameters.isBreakImportIfAlreadyImportedPhotoFound() ) {
					final TranslatableMessage message1 = new TranslatableMessage( "Already imported photo #$1 found. Skipping the import of the rest photos of $2", getServices() )
						.addIntegerParameter( remotePhotoSitePhotoId )
						.string( remotePhotoSiteUserPageLink )
						;

					job.addJobRuntimeLogMessage( message1 );

					return new RemotePhotoSitePhotosFromPageToImport( result, true );
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

			final RemotePhotoData cachedRemotePhotoData = getCachedRemotePhotoSitePhotos( remotePhotoSitePhotoId, cachedLocallyRemotePhotoDatas );

			if ( cachedRemotePhotoData != null ) {

				result.add( cachedRemotePhotoData );

				log.debug( String.format( "Photo %d of %s has been found in the local cache.", remotePhotoSitePhotoId, remotePhotoSiteUserPageLink ) );

				final TranslatableMessage translatableMessage = new TranslatableMessage( "Found in the local cache: $1", services )
					.string( remoteContentHelper.getPhotoCardLink( cachedRemotePhotoData ) )
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

				result.addAll( makeImportPhotoFromRemotePhotoSite( remoteUser, remotePhotoSitePhotoId ) );
			}
		}

		return new RemotePhotoSitePhotosFromPageToImport( result, false );
	}

	private RemotePhotoData getCachedRemotePhotoSitePhotos( final int remotePhotoSitePhotoId, final List<RemotePhotoData> cachedLocallyRemotePhotoDatas ) {
		for ( final RemotePhotoData cachedLocallyRemotePhotoData : cachedLocallyRemotePhotoDatas ) {
			if ( cachedLocallyRemotePhotoData.getPhotoId() == remotePhotoSitePhotoId ) {
				return cachedLocallyRemotePhotoData;
			}
		}
		return null;
	}

	private String getUserName( final RemoteUser remoteUser ) throws IOException {

		final String remotePhotoSiteUserName = remoteContentHelper.extractUserNameFromRemoteSite( remoteUser );

		if ( StringUtils.isEmpty( remotePhotoSiteUserName ) ) {
			final String message = String.format( "Can not extract a name of a remote photo site user #%s from a page content. Photos import of the user will be skipped.", remoteContentHelper.getUserCardLink( remoteUser ) );
			log.error( message );

			final TranslatableMessage translatableMessage = new TranslatableMessage( "Can not extract a name of a remote photo site user #$1 from page content. Photos import of the user will be skipped.", services )
				.string( remoteContentHelper.getUserCardLink( remoteUser ) )
				;
			job.addJobRuntimeLogMessage( translatableMessage );
		}

		return remotePhotoSiteUserName;
	}

	private List<RemotePhotoData> getCachedRemotePhotosData( final RemoteUser remoteUser ) throws IOException {

		try {
			return remotePhotoSiteCacheXmlUtils.getPhotosFromRemoteSiteUserInfoFile( importParameters.getImportSource(), remoteUser, services, job.getJobEnvironment().getLanguage() );
		} catch ( DocumentException e ) {
			/*final TranslatableMessage translatableMessage = new TranslatableMessage( "Error reading user info file: $1<br />$2", services )
				.string( remotePhotoSiteCacheXmlUtils.getUserInfoFile( remoteUser ).getAbsolutePath() )
				.string( e.getMessage() )
				;*/
			//			job.addJobRuntimeLogMessage( translatableMessage );
			logger.logErrorReadingUserDataFile( remoteUser, remotePhotoSiteCacheXmlUtils.getUserInfoFile( remoteUser ), e );

			log.error( String.format( "Error reading user info file: %s<br />", remotePhotoSiteCacheXmlUtils.getUserInfoFile( remoteUser ).getAbsolutePath() ), e );

			throw new BaseRuntimeException( e );
		}
	}

	private List<RemotePhotoSitePhotoDiskEntry> downloadRemotePhotoSitePhotoAndCache( final List<RemotePhotoData> remotePhotoDatas ) throws IOException {

		final int toAddCount = remotePhotoDatas.size();

		if ( toAddCount > 0 ) {
			job.addJobRuntimeLogMessage( new TranslatableMessage( "$1 images about to be downloaded", services ).addIntegerParameter( toAddCount ) );
		}

		final List<RemotePhotoSitePhotoDiskEntry> result = newArrayList();
		int counter = 1;
		for ( final RemotePhotoData remotePhotoData : remotePhotoDatas ) {

			final RemotePhotoSiteImage remotePhotoSiteImage = remotePhotoData.getRemotePhotoSiteImage();
			log.debug( String.format( "Getting photo %s content", remotePhotoSiteImage.getImageUrl() ) );
			job.addJobRuntimeLogMessage( new TranslatableMessage( "$1 / $2: Getting image '$3' content", services )
				.addIntegerParameter( counter )
				.addIntegerParameter( toAddCount )
				.link( remotePhotoData.getRemotePhotoSiteImage().getImageUrl(), remotePhotoData.getRemotePhotoSiteImage().getImageUrl() )
			);

			final String imageContent = remoteContentHelper.getImageContentFromUrl( remotePhotoSiteImage.getImageUrl() );
			if ( imageContent == null ) {
				remotePhotoData.setHasError( true );
				job.addJobRuntimeLogMessage( new TranslatableMessage( "Can not get remote photo site image content: '$1'", services ).string( remotePhotoData.getRemotePhotoSiteImage().getImageUrl() ) );
				continue;
			}

			final ImageToImport imageToImport = remotePhotoSiteCacheXmlUtils.createRemotePhotoCacheEntry( remotePhotoData, imageContent );

			result.add( new RemotePhotoSitePhotoDiskEntry( remotePhotoData, imageToImport ) );

			counter++;
		}

		return result;
	}

	private List<RemotePhotoSiteDBEntry> preparePhotosToImport( final RemoteUser remoteUser, final User localUser, final List<RemotePhotoSitePhotoDiskEntry> remotePhotoSitePhotoDiskEntries ) throws IOException {

		final List<RemotePhotoSiteDBEntry> photosToImport = newArrayList();

		for ( final RemotePhotoSitePhotoDiskEntry remotePhotoSitePhotoDiskEntry : remotePhotoSitePhotoDiskEntries ) {

			final RemotePhotoData remotePhotoData = remotePhotoSitePhotoDiskEntry.getRemotePhotoData();
			final ImageToImport imageToImport = remotePhotoSitePhotoDiskEntry.getImageToImport();

			final ImageToImportData imageToImportData = new ImageToImportData( imageToImport );
			imageToImportData.setUser( localUser );
			imageToImportData.setName( remotePhotoData.getName() );
			imageToImportData.setRemotePhotoSiteSeries( remotePhotoData.getRemotePhotoSiteSeries() );

			final RemotePhotoSiteCategory photosightCategory = remotePhotoData.getRemotePhotoSiteCategory();
			final DateUtilsService dateUtilsService = services.getDateUtilsService();

			final Genre genre = services.getRemotePhotoCategoryService().getMappedGenreOrOther( photosightCategory );
			final String siteUrl = remoteContentHelper.getRemotePhotoSiteHost();
			final String description = String.format( "Imported from '%s' at %s ( %s ). Photo category: %s."
				, siteUrl
				, dateUtilsService.formatDateTime( dateUtilsService.getCurrentTime() )
				, remoteContentHelper.getPhotoCardLink( remotePhotoData )
				, genre.getName()
			);
			imageToImportData.setPhotoDescription( description );

			final String keywords = StringUtilities.truncateString( String.format( "%s, %s, %s", siteUrl, remoteUser.getName(), imageToImportData.getName() ), 255 );
			imageToImportData.setPhotoKeywords( keywords );
			imageToImportData.setUploadTime( remotePhotoData.getUploadTime() );
			imageToImportData.setImportId( remotePhotoSitePhotoDiskEntry.getRemotePhotoData().getPhotoId() );

			photosToImport.add( new RemotePhotoSiteDBEntry( remotePhotoSitePhotoDiskEntry, imageToImportData ) );
		}

		return photosToImport;
	}

	private List<Integer> extractUserPhotosIdsFromPage( final String remotePhotoSiteUserId, final String userPageContent ) {
		final List<Integer> result = newArrayList();

		final Pattern pattern = Pattern.compile( remotePhotoSitePageContentDataExtractor.getPhotoIdRegex( remotePhotoSiteUserId ) );
		final Matcher matcher = pattern.matcher( userPageContent );

		while ( matcher.find() && ! job.hasJobFinishedWithAnyResult() ) {
			result.add( NumberUtils.convertToInt( matcher.group( 1 ) ) );
		}

		return result;
	}

	private List<RemotePhotoData> makeImportPhotoFromRemotePhotoSite( final RemoteUser remoteUser, final int remotePhotoSitePhotoId ) throws IOException {

		final String photoPageContent = remoteContentHelper.getPhotoPageContent( remoteUser, remotePhotoSitePhotoId );
		if ( StringUtils.isEmpty( photoPageContent ) ) {
			logPhotoSkipping( remoteUser, remotePhotoSitePhotoId, "Can't load photo page content." );
			return newArrayList();
		}

		final List<RemotePhotoSiteImage> remotePhotoSiteImages = remotePhotoSitePageContentDataExtractor.extractImageUrl( remoteUser.getId(), remotePhotoSitePhotoId, photoPageContent );
		if ( remotePhotoSiteImages == null || remotePhotoSiteImages.isEmpty() ) {
			logPhotoSkipping( remoteUser, remotePhotoSitePhotoId, "Can not extract photo image URL from page content." );
			return newArrayList();
		}

		final RemotePhotoSiteCategory photosightCategory = RemotePhotoSiteCategory.getById( importParameters.getImportSource(), remotePhotoSitePageContentDataExtractor.extractPhotoCategoryId( photoPageContent ) );
		if( photosightCategory == null ) {
			logPhotoSkipping( remoteUser, remotePhotoSitePhotoId, "Can not extract photo category from page content." );
			return newArrayList();
		}

		final List<RemotePhotoData> result = newArrayList( );

		int numberInSeries = 1;

		final int total = remotePhotoSiteImages.size();
		int counter = 1;

		for ( final RemotePhotoSiteImage remotePhotoSiteImage : remotePhotoSiteImages ) {

			/*final TranslatableMessage translatableMessage = new TranslatableMessage( "Collecting data of remote photo site photo #$1 ( $2 )", services )
					.addIntegerParameter( remotePhotoSitePhotoId )
					.string( remotePhotoSiteImage.getImageUrl() )
					;
			job.addJobRuntimeLogMessage( translatableMessage );*/

			final RemotePhotoData remotePhotoData = new RemotePhotoData( remoteUser, remotePhotoSitePhotoId, photosightCategory );
			final String photoName = remotePhotoSitePageContentDataExtractor.extractPhotoName( photoPageContent );

			if ( remotePhotoSiteImage.hasSeries() ) {
				remotePhotoData.setName( String.format( "%s #%d", photoName, numberInSeries ) );

				final RemotePhotoSiteSeries series = remotePhotoSiteImage.getSeries();
				series.setName( photoName );

				remotePhotoData.setRemotePhotoSiteSeries( series );
			} else {
				remotePhotoData.setName( photoName );
			}

			final Date uploadTime = remotePhotoSitePageContentDataExtractor.extractPhotoUploadTime( photoPageContent, services );
			if ( uploadTime != null ) {
				remotePhotoData.setUploadTime( uploadTime );
			} else {
				remotePhotoData.setUploadTime( services.getRandomUtilsService().getRandomDate( firstPhotoUploadTime, services.getDateUtilsService().getCurrentTime() ) );

				/*final TranslatableMessage translatableMessage1 = new TranslatableMessage( "$1: can not get upload time from remote photo page. Random time is used.", services )
					.string( remoteContentHelper.getPhotoCardLink( remotePhotoSitePhoto ) )
					;
				job.addJobRuntimeLogMessage( translatableMessage1 );*/
			}

			remotePhotoData.setRemotePhotoSiteImage( remotePhotoSiteImage );
			remotePhotoData.setNumberInSeries( numberInSeries );

			if ( importParameters.isImportComments() ) {
				final List<String> comments = remotePhotoSitePageContentDataExtractor.extractComments( photoPageContent );
				remotePhotoData.setComments( comments );
			}

			remotePhotoData.setCached( false );

			log.debug( String.format( "Photo %d has been downloaded from remote photo site", remotePhotoData.getPhotoId() ) );

			final TranslatableMessage translatableMessage2 = new TranslatableMessage( "$1 / $2 Got data of '$3': $4 of $5, photo category: $6", services )
				.addIntegerParameter( counter )
				.addIntegerParameter( total )
				.string( remoteContentHelper.getRemotePhotoSiteHost() )
				.string( remoteContentHelper.getPhotoCardLink( remotePhotoData ) )
				.string( remoteContentHelper.getUserCardLink( remoteUser ) )
				.string( remoteContentHelper.getPhotoCategoryLink( remotePhotoData.getRemotePhotoSiteCategory(), services.getEntityLinkUtilsService(), services.getGenreService(), importParameters.getLanguage(), services.getRemotePhotoCategoryService() ) )
				;
			job.addJobRuntimeLogMessage( translatableMessage2 );
			counter++;

			result.add( remotePhotoData );

			numberInSeries++;
		}

		return result;
	}

	private void logPhotoSkipping( final RemoteUser remoteUser, final int remotePhotoSitePhotoId, final String s ) {

		final TranslatableMessage translatableMessage = new TranslatableMessage( "$1 User: $2; photo: $3. Photo import skipped.", services )
			.string( s )
			.string( remoteUser.toString() ) // TODO: ?
			.string( remoteContentHelper.getPhotoCardLink( remoteUser.getId(), remotePhotoSitePhotoId ) )
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

		final RemotePhotoData remotePhotoData = dbEntry.getRemotePhotoSitePhotoDiskEntry().getRemotePhotoData();
		log.debug( String.format( "Importing comments for photo %d", remotePhotoData.getPhotoId() ) );

		final Photo photo = dbEntry.getImageToImportData().getPhoto();

		final Date photoUploadTime = photo.getUploadTime();

		final Date now = dateUtilsService.getCurrentDate();

		final List<String> comments = remotePhotoData.getComments();

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

	private String getUserCardFileName( final RemoteUser remoteUser, final int page ) {
		return String.format( "userCard_%s_page_%d", remoteUser.getId(), page );
	}

	private String getPhotoCardFileName( final RemotePhotoData remotePhotoData ) {
		return String.format( "photoCard_%d", remotePhotoData.getPhotoId() );
	}

	private User tryToFindThisUser( final RemoteUser remoteUser ) {

		final String userLogin = createLoginForRemotePhotoSiteUser( remoteUser.getId() );
		final User foundByLoginUser = services.getUserService().loadByLogin( userLogin );
		if ( foundByLoginUser != null ) {

			final TranslatableMessage translatableMessage = new TranslatableMessage( "Existing user found. Login: $1", services )
				.addUserCardLinkParameter( foundByLoginUser )
				;
			job.addJobRuntimeLogMessage( translatableMessage );

			return foundByLoginUser;
		}

		final User foundByNameUser = services.getUserService().loadByName( remoteUser.getName() );
		if ( foundByNameUser != null ) {

			final TranslatableMessage translatableMessage = new TranslatableMessage( "Existing user found. Name: $1", services )
				.addUserCardLinkParameter( foundByNameUser )
				;
			job.addJobRuntimeLogMessage( translatableMessage );

			return foundByNameUser;
		}

		return null;
	}

	private User findExistingOrCreateUser( final RemoteUser remoteUser, final RemoteSitePhotosImportParameters parameters ) throws IOException {

		final String userName = getUserName( remoteUser );
		if ( StringUtils.isEmpty( userName ) ) {
			return null;
		}

		remoteUser.setName( userName );

		final String userLogin = createLoginForRemotePhotoSiteUser( remoteUser.getId() );
		final User foundUser = tryToFindThisUser( remoteUser );
		if ( foundUser != null ) {
			return foundUser;
		}

		final User user = services.getFakeUserService().getRandomUser();

		user.setLogin( userLogin );
		user.setName( userName );
		user.setMembershipType( parameters.getMembershipType() );
		user.setGender( parameters.getUserGender() );
		user.setSelfDescription( String.format( "A user of %s: %s ( %s )"
			, remoteContentHelper.getRemotePhotoSiteHost()
			, remoteUser.getId()
			, remoteContentHelper.getUserCardUrl( remoteUser.getId(), 1 ) )
		);

		if ( ! services.getUserService().save( user ) ) {
			throw new BaseRuntimeException( "Can not create user" );
		}

		final TranslatableMessage translatableMessage = new TranslatableMessage( "New user has been created: $1", services )
			.addUserCardLinkParameter( user )
			;
		job.addJobRuntimeLogMessage( translatableMessage );

		return user;
	}

	private class RemotePhotoSitePhotosFromPageToImport {

		private final List<RemotePhotoData> remotePhotoDatas;
		private final boolean breakImportAfterThisPageProcessed;

		private RemotePhotoSitePhotosFromPageToImport( final List<RemotePhotoData> remotePhotoDatas, final boolean breakImportAfterThisPageProcessed ) {
			this.remotePhotoDatas = remotePhotoDatas;
			this.breakImportAfterThisPageProcessed = breakImportAfterThisPageProcessed;
		}

		public List<RemotePhotoData> getRemotePhotoDatas() {
			return remotePhotoDatas;
		}

		public boolean isBreakImportAfterThisPageProcessed() {
			return breakImportAfterThisPageProcessed;
		}
	}
}

