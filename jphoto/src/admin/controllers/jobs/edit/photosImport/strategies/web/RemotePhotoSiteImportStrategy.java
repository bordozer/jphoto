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
import java.util.Iterator;
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

		final List<RemotePhotoData> cachedUserPhotos = getCachedRemotePhotosData( remoteUser );

		int page = 1;

		int userPagesToProcessCount = getUserPagesToProcessCount( remoteUser );

		while ( !job.isFinished() && !job.hasJobFinishedWithAnyResult() && page <= userPagesToProcessCount ) {

			final List<Integer> collectedRemotePhotoIds = collectPhotoIdsFromPage( remoteUser, page );

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			if ( collectedRemotePhotoIds.size() == 0 ) {
				logger.logNoPhotosFoundOnPage( remoteUser, page );

				continue;
			}

			final boolean breakImportAfterThisPageProcessed = filterOutAlreadyDownloadedPhotos( remoteUser, collectedRemotePhotoIds, user );

			final List<RemotePhotoData> photosToImportData = collectRemotePhotosData( remoteUser, collectedRemotePhotoIds, cachedUserPhotos );

			filterOutPhotosWithWrongCategories( photosToImportData );

			final List<RemotePhotoData> cachedRemotePhotosData = getCachedRemotePhotosData( photosToImportData );
			final List<RemotePhoto> cachedRemotePhotos = getCachedRemotePhotos( cachedRemotePhotosData );

			final List<RemotePhotoData> notCachedRemotePhotosData = getNotCachedRemotePhotosData( photosToImportData );
			final List<RemotePhoto> notCachedPhotos = downloadNotCachedPhotos( remoteUser, notCachedRemotePhotosData );

			cachedUserPhotos.addAll( notCachedRemotePhotosData );
			remotePhotoSiteCacheXmlUtils.recreateUserCacheFile( remoteUser, cachedUserPhotos, services.getDateUtilsService() );

			final List<RemotePhoto> remotePhotosToImport = newArrayList( cachedRemotePhotos );
			remotePhotosToImport.addAll( notCachedPhotos );

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			final List<RemotePhotoSiteDBEntry> entries = preparePhotosToImport( remoteUser, user, remotePhotosToImport );

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

			if ( breakImportAfterThisPageProcessed ) {
				job.increment( userPagesToProcessCount - page + 1 );
				return;
			}

			page++;
			job.increment();
		}
	}

	private List<Integer> collectPhotoIdsFromPage( final RemoteUser remoteUser, final int page ) {

		final String userPageContent = remoteContentHelper.getUserPageContent( page, remoteUser.getId() );
		if ( StringUtils.isEmpty( userPageContent ) ) {
			// log.info( "Can not load remote photo site user first page - skipping import user's photos" );

			return newArrayList();
		}

		return collectUserPhotosIdsFromPage( remoteUser.getId(), userPageContent );
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

	private int getUserPagesToProcessCount( final RemoteUser remoteUser ) {

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

	private List<RemotePhoto> downloadNotCachedPhotos( final RemoteUser remoteUser, final List<RemotePhotoData> remotePhotosData ) throws IOException {

		remotePhotoSiteCacheXmlUtils.prepareUserGenreFolders( remoteUser, remotePhotosData );

		return downloadRemotePhotoSitePhotoAndCache( remotePhotosData );
	}

	private List<RemotePhoto> getCachedRemotePhotos( final List<RemotePhotoData> cachedRemotePhotosData ) throws IOException {

		final List<RemotePhoto> result = newArrayList();

		for ( final RemotePhotoData remotePhotoData : cachedRemotePhotosData ) {
			final File imageFile = remotePhotoSiteCacheXmlUtils.getRemotePhotoCacheFile( remotePhotoData );

			if ( ! imageFile.exists() ) {
				continue;
			}

			final ImageToImport imageToImport = new ImageToImport( importParameters.getImportSource(), remotePhotoData.getRemotePhotoSiteCategory().getKey(), imageFile );
			result.add( new RemotePhoto( remotePhotoData, imageToImport ) );
		}

		return result;
	}

	private List<RemotePhotoData> getNotCachedRemotePhotosData( final List<RemotePhotoData> remotePhotosData ) {
		final List<RemotePhotoData> notCachedRemotePhotosData = newArrayList( remotePhotosData );
		CollectionUtils.filter( notCachedRemotePhotosData, new Predicate<RemotePhotoData>() {
			@Override
			public boolean evaluate( final RemotePhotoData remotePhotoData ) {
				return !remotePhotoData.isCached();
			}
		} );

		return notCachedRemotePhotosData;
	}

	private List<RemotePhotoData> getCachedRemotePhotosData( final List<RemotePhotoData> remotePhotoDatas ) {
		final List<RemotePhotoData> cachedRemotePhotosData = newArrayList( remotePhotoDatas );
		CollectionUtils.filter( cachedRemotePhotosData, new Predicate<RemotePhotoData>() {
			@Override
			public boolean evaluate( final RemotePhotoData remotePhotoData ) {
				return remotePhotoData.isCached();
			}
		} );
		return cachedRemotePhotosData;
	}

	private boolean filterOutAlreadyDownloadedPhotos( final RemoteUser remoteUser, final List<Integer> remotePhotoIds, final User user ) {

		final JobHelperService jobHelperService = getServices().getJobHelperService();
		final String remoteUserPageLink = remoteContentHelper.getRemoteUserCardLink( remoteUser );

		final Iterator<Integer> iterator = remotePhotoIds.iterator();
		while ( iterator.hasNext() ) {

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			final int remotePhotoId = iterator.next();

			if ( jobHelperService.doesUserPhotoExist( user.getId(), remotePhotoId ) ) {

				if ( importParameters.isBreakImportIfAlreadyImportedPhotoFound() ) {

					logger.logSkippingTheRestPhotosBecauseAlreadyImportedPhotoFound( remoteUserPageLink, remotePhotoId );

					iterator.remove();

					return true;
				}

				logger.logSkippingPhotoImportBecauseItHasBeenAlreadyImported( remoteUserPageLink, remotePhotoId );

				iterator.remove();
			}
		}

		return false;
	}

	private List<RemotePhotoData> collectRemotePhotosData( final RemoteUser remoteUser, final List<Integer> remotePhotoSitePhotosIds, final List<RemotePhotoData> cachedLocallyRemotePhotoDatas ) throws IOException {

		final List<RemotePhotoData> result = newArrayList();

		final String remotePhotoSiteUserPageLink = remoteContentHelper.getRemoteUserCardLink( remoteUser );

		for ( final int remotePhotoSitePhotoId : remotePhotoSitePhotosIds ) {

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			final RemotePhotoData cachedRemotePhotoData = getCachedRemotePhotos( remotePhotoSitePhotoId, cachedLocallyRemotePhotoDatas );

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

		return result;
	}

	private RemotePhotoData getCachedRemotePhotos( final int remotePhotoSitePhotoId, final List<RemotePhotoData> cachedLocallyRemotePhotoDatas ) {
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
			final String message = String.format( "Can not extract a name of a remote photo site user #%s from a page content. Photos import of the user will be skipped.", remoteContentHelper.getRemoteUserCardLink( remoteUser ) );
			log.error( message );

			final TranslatableMessage translatableMessage = new TranslatableMessage( "Can not extract a name of a remote photo site user #$1 from page content. Photos import of the user will be skipped.", services )
				.string( remoteContentHelper.getRemoteUserCardLink( remoteUser ) )
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

	private List<RemotePhoto> downloadRemotePhotoSitePhotoAndCache( final List<RemotePhotoData> remotePhotoDatas ) throws IOException {

		final int toAddCount = remotePhotoDatas.size();

		if ( toAddCount > 0 ) {
			job.addJobRuntimeLogMessage( new TranslatableMessage( "$1 images about to be downloaded", services ).addIntegerParameter( toAddCount ) );
		}

		final List<RemotePhoto> result = newArrayList();
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

			result.add( new RemotePhoto( remotePhotoData, imageToImport ) );

			counter++;
		}

		return result;
	}

	private List<RemotePhotoSiteDBEntry> preparePhotosToImport( final RemoteUser remoteUser, final User localUser, final List<RemotePhoto> remotePhotoSitePhotoDiskEntries ) throws IOException {

		final List<RemotePhotoSiteDBEntry> photosToImport = newArrayList();

		for ( final RemotePhoto remotePhoto : remotePhotoSitePhotoDiskEntries ) {

			final RemotePhotoData remotePhotoData = remotePhoto.getRemotePhotoData();
			final ImageToImport imageToImport = remotePhoto.getImageToImport();

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
			imageToImportData.setImportId( remotePhoto.getRemotePhotoData().getPhotoId() );

			photosToImport.add( new RemotePhotoSiteDBEntry( remotePhoto, imageToImportData ) );
		}

		return photosToImport;
	}

	private List<Integer> collectUserPhotosIdsFromPage( final String remotePhotoSiteUserId, final String userPageContent ) {
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
				.string( remoteContentHelper.getRemoteUserCardLink( remoteUser ) )
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

		final RemotePhotoData remotePhotoData = dbEntry.getRemotePhoto().getRemotePhotoData();
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
}

