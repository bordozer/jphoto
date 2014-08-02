package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.ImageToImport;
import admin.controllers.jobs.edit.photosImport.ImageToImportData;
import admin.controllers.jobs.edit.photosImport.RemotePhotoSiteSeries;
import admin.controllers.jobs.edit.photosImport.importParameters.AbstractImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.RemoteSitePhotosImportParameters;
import admin.controllers.jobs.edit.photosImport.strategies.AbstractPhotoImportStrategy;
import admin.controllers.jobs.edit.photosImport.strategies.web.download.RemotePhotoDownloader;
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

	private final AbstractRemotePhotoSitePageContentDataExtractor pageContentDataExtractor;
	private final AbstractRemotePhotoSiteUrlHelper remoteContentHelper;
	private final RemotePhotoSiteCacheXmlUtils xmlUtils;

	final LogHelper log = new LogHelper( RemotePhotoSiteImportStrategy.class );

	private final PhotosImportLogger logger;

	public RemotePhotoSiteImportStrategy( final AbstractJob job, final AbstractImportParameters parameters, final Services services ) {
		super( job, services, new LogHelper( RemotePhotoSiteImportStrategy.class ), parameters.getLanguage() );

		importParameters = ( RemoteSitePhotosImportParameters ) parameters;

		this.pageContentDataExtractor = PhotosightContentDataExtractor.getInstance( importParameters.getImportSource() );
		this.remoteContentHelper = AbstractRemotePhotoSiteUrlHelper.getInstance( importParameters.getImportSource() );

		logger = new PhotosImportLogger( job, remoteContentHelper, services );

		xmlUtils = new RemotePhotoSiteCacheXmlUtils (
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

	@Override
	public int calculateTotalPagesToProcess() throws IOException {

		int totalPages = 0;

		final List<String> remotePhotoSiteUsersIds = importParameters.getRemoteUserIds();
		for ( final String remotePhotoSiteUserId : remotePhotoSiteUsersIds ) {

			final String userPageContent = remoteContentHelper.getUserPageContent( 1, remotePhotoSiteUserId );

			if ( StringUtils.isEmpty( userPageContent ) ) {
				logger.logErrorGettingUserPagesCount( remotePhotoSiteUserId );

				continue;
			}

			final int userPagesCount = pageContentDataExtractor.getRemoteUserPagesCount( userPageContent, remotePhotoSiteUserId );
			totalPages += userPagesCount;

			logger.logUserPagesCountToProcessHasGotFromThePageContext( remotePhotoSiteUserId, userPagesCount, totalPages );

			if ( job.getGenerationMonitor().getStatus().isNotActive() ) {
				break;
			}
		}

		return totalPages;
	}

	private void importRemotePhotoSiteUserPhotos( final RemoteUser remoteUser ) throws IOException, SaveToDBException {

		logger.logUserImportImportStart( remoteUser.getId() );

		final User user = findExistingOrCreateUser( remoteUser, importParameters );
		if ( user == null ) {
			logger.logUserCanNotBeCreated( remoteUser );
			return;
		}

		xmlUtils.initRemoteUserCacheFileStructure( remoteUser );

		final List<RemotePhotoData> cachedUserPhotosData = getCachedRemotePhotosData( remoteUser );

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

			logger.logFoundPhotosOnRemoteUserCardPage( remoteUser, page, collectedRemotePhotoIds.size() );

			final boolean breakImportAfterThisPageProcessed = filterOutAlreadyDownloadedPhotos( remoteUser, collectedRemotePhotoIds, user );

			final List<RemotePhotoData> photosToImportData = getRemotePhotosData( remoteUser, collectedRemotePhotoIds, cachedUserPhotosData );

			filterOutPhotosWithWrongCategories( photosToImportData );

			final RemotePhotoDownloader photoDownloader = new RemotePhotoDownloader( job, remoteUser, photosToImportData, xmlUtils, remoteContentHelper, importParameters.getImportSource(), services );

			cachedUserPhotosData.addAll( photoDownloader.getNotCachedRemotePhotosData() );
			xmlUtils.recreateUserCacheFile( remoteUser, cachedUserPhotosData, services.getDateUtilsService() );

			final List<RemotePhoto> remotePhotosToImport = newArrayList( photoDownloader.getRemotePhotos() );

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
			logger.logEmptyRemoteUserCardPageContent( remoteUser, page );

			return newArrayList();
		}

		final List<Integer> result = newArrayList();

		final Pattern pattern = Pattern.compile( pageContentDataExtractor.getPhotoIdRegex( remoteUser.getId() ) );
		final Matcher matcher = pattern.matcher( userPageContent );

		while ( matcher.find() && ! job.hasJobFinishedWithAnyResult() ) {
			result.add( NumberUtils.convertToInt( matcher.group( 1 ) ) );
		}

		return result;
	}

	private int getUserPagesToProcessCount( final RemoteUser remoteUser ) {

		final String userFirstPageContent = remoteContentHelper.getUserPageContent( 1, remoteUser.getId() );
		final boolean doesUserPageExist = StringUtils.isEmpty( userFirstPageContent );
		if ( doesUserPageExist ) {
			logger.logUserPageCountError( remoteUser );
			return 0;
		}

		final int totalPagesQty = pageContentDataExtractor.getRemoteUserPagesCount( userFirstPageContent, remoteUser.getId() );
		logger.logUserPageCountGotSuccessfully( remoteUser, totalPagesQty );

		return totalPagesQty;
	}

	private void filterOutPhotosWithWrongCategories( final List<RemotePhotoData> remotePhotoSitePagePhotos ) {
		CollectionUtils.filter( remotePhotoSitePagePhotos, new Predicate<RemotePhotoData>() {
			@Override
			public boolean evaluate( final RemotePhotoData remotePhotoData ) {
				return importParameters.getRemotePhotoSiteCategories().contains( remotePhotoData.getRemotePhotoSiteCategory() );
			}
		} );
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

	private List<RemotePhotoData> getRemotePhotosData( final RemoteUser remoteUser, final List<Integer> remotePhotoIds, final List<RemotePhotoData> cachedRemotePhotosData ) throws IOException {

		final List<RemotePhotoData> result = newArrayList();

		final int delayBetweenRequest = importParameters.getDelayBetweenRequest();

		for ( final int remotePhotoId : remotePhotoIds ) {

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			final RemotePhotoData cachedRemotePhotoData = findRemotePhotoInCache( remotePhotoId, cachedRemotePhotosData );
			if ( cachedRemotePhotoData != null ) {
				result.add( cachedRemotePhotoData );
				logger.logRemotePhotoHasBeenFoundInTheCache( remoteUser, cachedRemotePhotoData );

				continue;
			}

			if ( delayBetweenRequest > 0 ) {
				log.debug( String.format( "Waiting %s secs", delayBetweenRequest ) );
				try {
					Thread.sleep( delayBetweenRequest * 1000 );
				} catch ( InterruptedException e ) {
					log.error( e );
				}
			}

			result.addAll( collectRemotePhotosDataFromRemoteSite( remoteUser, remotePhotoId ) );
		}

		return result;
	}

	private RemotePhotoData findRemotePhotoInCache( final int remotePhotoSitePhotoId, final List<RemotePhotoData> cachedLocallyRemotePhotoDatas ) {
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
			return xmlUtils.getPhotosFromRemoteSiteUserInfoFile( importParameters.getImportSource(), remoteUser, services, job.getJobEnvironment().getLanguage() );
		} catch ( DocumentException e ) {
			logger.logErrorReadingUserDataFile( remoteUser, xmlUtils.getUserInfoFile( remoteUser ), e );

			throw new BaseRuntimeException( e );
		}
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

	private List<RemotePhotoData> collectRemotePhotosDataFromRemoteSite( final RemoteUser remoteUser, final int remotePhotoId ) throws IOException {

		final String photoCardPageContent = remoteContentHelper.getPhotoPageContent( remoteUser, remotePhotoId );

		if ( StringUtils.isEmpty( photoCardPageContent ) ) {
			logger.logPhotoSkipping( remoteUser, remotePhotoId, "Can't load photo page content." );
			return newArrayList();
		}

		final List<RemotePhotoSiteImage> remotePhotoImages = pageContentDataExtractor.extractImageUrl( remoteUser.getId(), remotePhotoId, photoCardPageContent );
		if ( remotePhotoImages == null || remotePhotoImages.isEmpty() ) {
			logger.logPhotoSkipping( remoteUser, remotePhotoId, "Can not extract photo image URL from page content." );
			return newArrayList();
		}

		final RemotePhotoSiteCategory photosightCategory = RemotePhotoSiteCategory.getById( importParameters.getImportSource(), pageContentDataExtractor.extractPhotoCategoryId( photoCardPageContent ) );
		if( photosightCategory == null ) {
			logger.logPhotoSkipping( remoteUser, remotePhotoId, "Can not extract photo category from page content." );
			return newArrayList();
		}

		final List<RemotePhotoData> result = newArrayList( );

		final int total = remotePhotoImages.size();
		int photoNumberInSeries = 1;

		for ( final RemotePhotoSiteImage remotePhotoSiteImage : remotePhotoImages ) {

			if ( remotePhotoSiteImage == null ) {
				photoNumberInSeries++;
				continue;
			}

			logger.logCollectingRemotePhotoDataForImage( remoteUser.getId(), remotePhotoId, remotePhotoSiteImage.getImageUrl() );

			final RemotePhotoData remotePhotoData = new RemotePhotoData( remoteUser, remotePhotoId, photosightCategory, remotePhotoSiteImage.getImageUrl() );
			final String photoName = pageContentDataExtractor.extractPhotoName( photoCardPageContent );

			if ( remotePhotoSiteImage.hasSeries() ) {
				remotePhotoData.setName( String.format( "%s #%d", photoName, photoNumberInSeries ) );

				final RemotePhotoSiteSeries series = remotePhotoSiteImage.getSeries();
				series.setName( photoName );

				remotePhotoData.setRemotePhotoSiteSeries( series );
			} else {
				remotePhotoData.setName( photoName );
			}

			final Date uploadTime = pageContentDataExtractor.extractPhotoUploadTime( photoCardPageContent, services );
			if ( uploadTime != null ) {
				remotePhotoData.setUploadTime( uploadTime );
			} else {
				remotePhotoData.setUploadTime( services.getRandomUtilsService().getRandomDate( firstPhotoUploadTime, services.getDateUtilsService().getCurrentTime() ) );
				logger.logErrorGettingPhotoUploadTime( remoteUser.getId(), remotePhotoId );
			}

			remotePhotoData.setRemotePhotoSiteImage( remotePhotoSiteImage );
			remotePhotoData.setNumberInSeries( photoNumberInSeries );

			if ( importParameters.isImportComments() ) {
				final List<String> comments = pageContentDataExtractor.extractComments( photoCardPageContent );
				remotePhotoData.setComments( comments );
			}

			remotePhotoData.setCached( false );

			result.add( remotePhotoData );

			logger.logSuccessDataCollectingOfRemotePhoto( photoNumberInSeries, total, remotePhotoData, remoteUser, importParameters.getLanguage() );

			photoNumberInSeries++;
		}

		return result;
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

	public static String createLoginForRemotePhotoSiteUser( final String remotePhotoSiteUserId ) {
		return String.format( "%s%s", REMOTE_PHOTO_SITE_USER_LOGIN_PREFIX, remotePhotoSiteUserId );
	}
}

