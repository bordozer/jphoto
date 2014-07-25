package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.ImageDiscEntry;
import admin.controllers.jobs.edit.photosImport.ImageToImport;
import admin.controllers.jobs.edit.photosImport.importParameters.AbstractImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.PhotosightImportParameters;
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

public class PhotosightImportStrategy extends AbstractPhotoImportStrategy {

	public static final String USERS = "users";
	public static final String PHOTOS = "photos";
	public static final String PHOTOSIGHT_USER_LOGIN_PREFIX = "PS_";

	private PhotosightImportParameters importParameters;

	final LogHelper log = new LogHelper( PhotosightImportStrategy.class );

	private final Date firstPhotoUploadTime;

	public PhotosightImportStrategy( final AbstractJob job, final AbstractImportParameters parameters, final Services services ) {
		super( job, services, new LogHelper( PhotosightImportStrategy.class ), parameters.getLanguage() );

		importParameters = ( PhotosightImportParameters ) parameters;

		firstPhotoUploadTime = getServices().getJobHelperService().getFirstPhotoUploadTime();
	}

	@Override
	public void doImport() throws IOException, SaveToDBException {

		for ( final String photosightUserId : importParameters.getPhotosightUserIds() ) {

			final PhotosightUser photosightUser = new PhotosightUser( photosightUserId );

			importPhotosightUserPhotos( photosightUser );

			if( job.hasJobFinishedWithAnyResult() ) {
				break;
			}
		}
	}

	private void importPhotosightUserPhotos( final PhotosightUser photosightUser ) throws IOException, SaveToDBException {

		final User user = findByNameOrCreateUser( photosightUser, importParameters );
		if ( user == null ) {
			log.error( String.format( "%s can not be created. Skipping user's photos import.", photosightUser ) );
			return;
		}

		prepareFolderForImageDownloading( photosightUser );

		final List<PhotosightPhoto> cachedLocallyPhotosightPhotos = getCachedLocallyPhotosightPhotos( photosightUser );

		int page = 1;

		final String userFirstPageContent = importParameters.getRemoteContentHelper().getUserPageContent( 1, photosightUser.getId() );
		if ( StringUtils.isEmpty( userFirstPageContent ) ) {
			return; // can not load page - just skipping
		}

		final int userPagesQty = PhotosightContentHelper.getTotalPagesQty( userFirstPageContent, photosightUser.getId() );

		while ( ! job.isFinished() && ! job.hasJobFinishedWithAnyResult() && page <= userPagesQty ) {

			log.debug( String.format( "Getting page %d context of %s", page, importParameters.getRemoteContentHelper().getPhotosightUserPageLink( photosightUser ) ) );

			final String userPageContent = importParameters.getRemoteContentHelper().getUserPageContent( page, photosightUser.getId() );
			if ( StringUtils.isEmpty( userPageContent ) ) {

				log.info( "Can not load photosight user first page - skipping import user's photos" );

				continue;
			}

			final List<Integer> photosightPagePhotosIds = extractUserPhotosIdsFromPage( userPageContent );

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			if ( photosightPagePhotosIds.size() == 0 ) {
				final String userCardFileName = getUserCardFileName( photosightUser, page );
				final File file = PhotosightImageFileUtils.writePageContentToFile( userCardFileName, userPageContent );

				final TranslatableMessage translatableMessage = new TranslatableMessage( "No photo have been found on page $1. User page content saved. See $2", services )
					.addIntegerParameter( page )
					.string( file.getAbsolutePath() )
					;
				job.addJobRuntimeLogMessage( translatableMessage );

				log.info( String.format( "No photo have been found on page %d. User page content saved. See %s", page, file.getCanonicalPath() ) );

				continue;
			}

			final PhotosightPhotosFromPageToImport photosightPhotosToImport = getPhotosightPhotosToImport( photosightUser, photosightPagePhotosIds, cachedLocallyPhotosightPhotos, user );
			final List<PhotosightPhoto> photosightPagePhotos = photosightPhotosToImport.getPhotosightPhotos();

			filterDownloadedPhotosByPhotosightCategories( photosightPagePhotos );

			final List<PhotosightPhotoOnDisk> photosightPhotosOnDisk = getPhotosightPhotoOnDisk( photosightUser, photosightPagePhotos, cachedLocallyPhotosightPhotos );

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			final List<PhotosightDBEntry> entries = prepareImagesToImport( photosightUser, user, photosightPhotosOnDisk );

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

	private void filterDownloadedPhotosByPhotosightCategories( final List<PhotosightPhoto> photosightPagePhotos ) {
		CollectionUtils.filter( photosightPagePhotos, new Predicate<PhotosightPhoto>() {
			@Override
			public boolean evaluate( final PhotosightPhoto photosightPhoto ) {
				return importParameters.getPhotosightCategories().contains( photosightPhoto.getPhotosightCategory() );
			}
		} );
	}

	private List<PhotosightPhotoOnDisk> getPhotosightPhotoOnDisk( final PhotosightUser photosightUser, final List<PhotosightPhoto> photosightPagePhotos, final List<PhotosightPhoto> cachedLocallyPhotosightPhotos ) throws IOException {
		final List<PhotosightPhotoOnDisk> photosightPhotosOnDisk = newArrayList();
		final List<PhotosightPhotoOnDisk> notCachedPhotosightPhotosOnDisk = getNotCachedPhotosightPhotosOnDisk( photosightUser, cachedLocallyPhotosightPhotos, photosightPagePhotos );
		final List<PhotosightPhotoOnDisk> cachedPhotosightPhotosOnDisk = getCachedPhotosightPhotosOnDisk( photosightPagePhotos );

		photosightPhotosOnDisk.addAll( notCachedPhotosightPhotosOnDisk );
		photosightPhotosOnDisk.addAll( cachedPhotosightPhotosOnDisk );
		return photosightPhotosOnDisk;
	}

	private PhotosightPhotosFromPageToImport getPhotosightPhotosToImport( final PhotosightUser photosightUser, final List<Integer> photosightPagePhotosIds, final List<PhotosightPhoto> cachedLocallyPhotosightPhotos, final User user ) throws IOException {
		final JobHelperService jobHelperService = getServices().getJobHelperService();
		final List<PhotosightPhoto> photosightPagePhotos = newArrayList();

		for ( final int photosightPhotoId : photosightPagePhotosIds ) {

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			final String photosightUserPageLink = importParameters.getRemoteContentHelper().getPhotosightUserPageLink( photosightUser );

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

			final PhotosightPhoto photosightPhoto;

			final PhotosightPhoto cachedPhotosightPhoto = getPhotosightPhotoFromCache( photosightPhotoId, cachedLocallyPhotosightPhotos );
			if ( cachedPhotosightPhoto != null ) {
				photosightPhoto = cachedPhotosightPhoto;
				log.debug( String.format( "Photo %d of %s has been found in the local cache.", photosightPhotoId, photosightUserPageLink ) );

				final TranslatableMessage translatableMessage = new TranslatableMessage( "Found in the local cache: $1", services )
					.string( importParameters.getRemoteContentHelper().getPhotosightPhotoPageLink( photosightPhoto ) )
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

				photosightPhoto = importPhotoFromPhotosight( photosightUser, photosightPhotoId );
			}

			if ( photosightPhoto != null ) {
				photosightPagePhotos.add( photosightPhoto );
			}
		}

		return new PhotosightPhotosFromPageToImport( photosightPagePhotos, false );
	}

	private List<PhotosightPhotoOnDisk> getCachedPhotosightPhotosOnDisk( final List<PhotosightPhoto> photosightPagePhotos ) throws IOException {
		final List<PhotosightPhoto> cachedPhotosightPagePhoto = newArrayList( photosightPagePhotos );
		CollectionUtils.filter( cachedPhotosightPagePhoto, new Predicate<PhotosightPhoto>() {
			@Override
			public boolean evaluate( final PhotosightPhoto photosightPhoto ) {
				return photosightPhoto.isCached();
			}
		} );

		final List<PhotosightPhotoOnDisk> result = newArrayList();

		for ( final PhotosightPhoto photosightPhoto : cachedPhotosightPagePhoto ) {
			final File imageFile = PhotosightImageFileUtils.getPhotosightPhotoLocalImageFile( photosightPhoto );

			if ( ! imageFile.exists() ) {
				continue;
			}

			final ImageDiscEntry imageDiscEntry = new ImageDiscEntry( imageFile, PhotosightImageFileUtils.getGenreDiscEntry( photosightPhoto.getPhotosightCategory() ) );
			result.add( new PhotosightPhotoOnDisk( photosightPhoto, imageDiscEntry ) );
		}

		return result;
	}

	private List<PhotosightPhotoOnDisk> getNotCachedPhotosightPhotosOnDisk( final PhotosightUser photosightUser, final List<PhotosightPhoto> cachedLocallyPhotosightPhotos, final List<PhotosightPhoto> photosightPagePhotos ) throws IOException {
		final List<PhotosightPhoto> notCachedPhotosightPagePhoto = newArrayList( photosightPagePhotos );
		CollectionUtils.filter( notCachedPhotosightPagePhoto, new Predicate<PhotosightPhoto>() {
			@Override
			public boolean evaluate( final PhotosightPhoto photosightPhoto ) {
				return !photosightPhoto.isCached();
			}
		} );

		PhotosightImageFileUtils.prepareUserGenreFolders( photosightUser, notCachedPhotosightPagePhoto );
		final List<PhotosightPhotoOnDisk> notCachedPhotosightPhotosOnDisk = downloadPhotosightPhotosOnDisk( notCachedPhotosightPagePhoto );
		cachePagePhotosightPhotosLocally( photosightUser, notCachedPhotosightPagePhoto, cachedLocallyPhotosightPhotos );
		return notCachedPhotosightPhotosOnDisk;
	}

	private PhotosightPhoto getPhotosightPhotoFromCache( final int photosightPhotoId, final List<PhotosightPhoto> cachedLocallyPhotosightPhotos ) {
		for ( final PhotosightPhoto cachedLocallyPhotosightPhoto : cachedLocallyPhotosightPhotos ) {
			if ( cachedLocallyPhotosightPhoto.getPhotoId() == photosightPhotoId ) {
				return cachedLocallyPhotosightPhoto;
			}
		}
		return null;
	}

	private String getUserName( final PhotosightUser photosightUser ) throws IOException {

		if ( StringUtils.isNotEmpty( photosightUser.getName() ) ) {
			return importParameters.getUserName();
		}

		final String photosightUserName = importParameters.getRemoteContentHelper().getPhotosightUserName( photosightUser );

		if ( StringUtils.isEmpty( photosightUserName ) ) {
			final String message = String.format( "Can not extract a name photosight user #%s from page content. Photos import of the user will be skipped.", importParameters.getRemoteContentHelper().getPhotosightUserPageLink( photosightUser ) );
			log.error( message );

			final TranslatableMessage translatableMessage = new TranslatableMessage( "Can not extract a name of photosight user #$1 from page content. Photos import of the user will be skipped.", services )
				.string( importParameters.getRemoteContentHelper().getPhotosightUserPageLink( photosightUser ) )
				;
			job.addJobRuntimeLogMessage( translatableMessage );
		}

		return photosightUserName;
	}

	private void prepareFolderForImageDownloading( final PhotosightUser photosightUser ) throws IOException {
		PhotosightImageFileUtils.createUserFolderForPhotoDownloading( photosightUser );
		PhotosightXmlUtils.createUserInfoFile( photosightUser );
	}

	private void cachePagePhotosightPhotosLocally( final PhotosightUser photosightUser, final List<PhotosightPhoto> photosightPagePhotos, final List<PhotosightPhoto> cachedLocallyPhotosightPhotos ) throws IOException {

		cachedLocallyPhotosightPhotos.addAll( photosightPagePhotos );

		PhotosightXmlUtils.cachePhotosightPhotosLocally( photosightUser, cachedLocallyPhotosightPhotos, services.getDateUtilsService() );
	}

	private List<PhotosightPhoto> getCachedLocallyPhotosightPhotos( final PhotosightUser photosightUser ) throws IOException {
		try {
			return PhotosightXmlUtils.getPhotosFromPhotosightUserInfoFile( photosightUser, services, job.getJobEnvironment().getLanguage() );
		} catch ( DocumentException e ) {
			final TranslatableMessage translatableMessage = new TranslatableMessage( "Error reading user info file: $1<br />$2", services )
				.string( PhotosightXmlUtils.getUserInfoFile( photosightUser ).getAbsolutePath() )
				.string( e.getMessage() )
				;
			job.addJobRuntimeLogMessage( translatableMessage );

			log.error( String.format( "Error reading user info file: %s<br />", PhotosightXmlUtils.getUserInfoFile( photosightUser ).getAbsolutePath() ), e );

			throw new BaseRuntimeException( e );
		}
	}

	private List<PhotosightPhotoOnDisk> downloadPhotosightPhotosOnDisk( final List<PhotosightPhoto> photosightPhotos ) throws IOException {

		final List<PhotosightPhotoOnDisk> result = newArrayList();

		for ( final PhotosightPhoto photosightPhoto : photosightPhotos ) {

			final String imageUrl = photosightPhoto.getImageUrl();
			log.debug( String.format( "Getting photo %s content", imageUrl ) );

			final String imageContent = importParameters.getRemoteContentHelper().getImageContentFromUrl( imageUrl );
			final ImageDiscEntry imageDiscEntry = PhotosightImageFileUtils.downloadPhotoOnDisk( photosightPhoto, imageContent );
			if ( imageDiscEntry == null ) {
				job.addJobRuntimeLogMessage( new TranslatableMessage( "Can not get photosight photo image content: '$1'", services ).string( photosightPhoto.getImageUrl() ) );
				continue;
			}
			result.add( new PhotosightPhotoOnDisk( photosightPhoto, imageDiscEntry ) );
		}

		return result;
	}

	private List<PhotosightDBEntry> prepareImagesToImport( final PhotosightUser photosightUser, final User user, final List<PhotosightPhotoOnDisk> photosightPhotosOnDisk ) throws IOException {
		final List<PhotosightDBEntry> imagesToImport = newArrayList();

		for ( final PhotosightPhotoOnDisk photosightPhotoOnDisk : photosightPhotosOnDisk ) {

			final PhotosightPhoto photosightPhoto = photosightPhotoOnDisk.getPhotosightPhoto();
			final ImageDiscEntry imageDiscEntry = photosightPhotoOnDisk.getImageDiscEntry();

			final ImageToImport imageToImport = new ImageToImport( imageDiscEntry );
			imageToImport.setUser( user );
			imageToImport.setName( photosightPhoto.getName() );

			final PhotosightCategory photosightCategory = photosightPhoto.getPhotosightCategory();
			final DateUtilsService dateUtilsService = services.getDateUtilsService();

			final GenreDiscEntry genreDiscEntry = PhotosightImageFileUtils.getGenreDiscEntry( photosightCategory );
			final String description = String.format( "Imported from photosight at %s ( %s ). Photo category: %s."
				, dateUtilsService.formatDateTime( dateUtilsService.getCurrentTime() ), importParameters.getRemoteContentHelper().getPhotosightPhotoPageLink( photosightPhoto ), genreDiscEntry.getName()
			);
			imageToImport.setPhotoDescription( description );

			final String keywords = StringUtilities.truncateString( String.format( "Photosight, %s, %s", photosightUser.getName(), imageToImport.getName() ), 255 );
			imageToImport.setPhotoKeywords( keywords );
			imageToImport.setUploadTime( photosightPhoto.getUploadTime() );
			imageToImport.setImportId( photosightPhotoOnDisk.getPhotosightPhoto().getPhotoId() );

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

		final List<String> photosightUserIds = importParameters.getPhotosightUserIds();
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
		return String.format( "%s%s", PHOTOSIGHT_USER_LOGIN_PREFIX, photosightUserId );
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

	private PhotosightPhoto importPhotoFromPhotosight( final PhotosightUser photosightUser, final int photosightPhotoId ) throws IOException {

		final String photoPageContent = importParameters.getRemoteContentHelper().getPhotoPageContent( photosightUser, photosightPhotoId );
		if ( StringUtils.isEmpty( photoPageContent ) ) {
			logPhotoSkipping( photosightUser, photosightPhotoId, "Can't load photo page content." );
			return null;
		}

		final String imageUrl = PhotosightContentDataExtractor.extractImageUrl( photosightPhotoId, photoPageContent );
		if ( StringUtils.isEmpty( imageUrl ) ) {
			logPhotoSkipping( photosightUser, photosightPhotoId, "Can not extract photo image URL from page content." );
			return null;
		}

		final PhotosightCategory photosightCategory = PhotosightCategory.getById( PhotosightContentHelper.extractPhotoCategoryId( photoPageContent ) );
		if( photosightCategory == null ) {
			logPhotoSkipping( photosightUser, photosightPhotoId, "Can not extract photosight category from page content." );
			return null;
		}

		final PhotosightPhoto photosightPhoto = new PhotosightPhoto( photosightUser, photosightPhotoId, photosightCategory );
		photosightPhoto.setName( PhotosightContentHelper.extractPhotoName( photoPageContent ) );

		final Date uploadTime = extractUploadTime( photoPageContent );
		if ( uploadTime != null ) {
			photosightPhoto.setUploadTime( uploadTime );
		} else {
			photosightPhoto.setUploadTime( services.getRandomUtilsService().getRandomDate( firstPhotoUploadTime, services.getDateUtilsService().getCurrentTime() ) );

			final TranslatableMessage translatableMessage = new TranslatableMessage( "$1: can not get upload time from photosight photo page. Random time is used.", services )
				.string( importParameters.getRemoteContentHelper().getPhotosightPhotoPageLink( photosightPhoto ) )
				;
			job.addJobRuntimeLogMessage( translatableMessage );
		}

		photosightPhoto.setImageUrl( imageUrl );

		if ( importParameters.isImportComments() ) {
			final List<String> comments = PhotosightContentHelper.extractComments( photoPageContent );
			photosightPhoto.setComments( comments );
		}

		photosightPhoto.setCached( false );

		log.debug( String.format( "Photo %d has been downloaded from photosight", photosightPhoto.getPhotoId() ) );

		final TranslatableMessage translatableMessage = new TranslatableMessage( "Downloaded from photosight: $1 of $2, photosight category: $3", services )
			.string( importParameters.getRemoteContentHelper().getPhotosightPhotoPageLink( photosightPhoto ) )
			.string( importParameters.getRemoteContentHelper().getPhotosightUserPageLink( photosightUser ) )
			.string( importParameters.getRemoteContentHelper().getPhotosightCategoryPageLink( photosightPhoto.getPhotosightCategory(), services.getEntityLinkUtilsService(), services.getGenreService(), importParameters.getLanguage() ) )
			;
		job.addJobRuntimeLogMessage( translatableMessage );

		return photosightPhoto;

	}

	private void logPhotoSkipping( final PhotosightUser photosightUser, final int photosightPhotoId, final String s ) {

		final TranslatableMessage translatableMessage = new TranslatableMessage( "$1 User: $2; photo: $3. Photo import skipped.", services )
			.string( s )
			.string( photosightUser.toString() ) // TODO: ?
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

		final PhotosightPhoto photosightPhoto = dbEntry.getPhotosightPhotoOnDisk().getPhotosightPhoto();
		log.debug( String.format( "Importing comments for photo %d", photosightPhoto.getPhotoId() ) );

		final Photo photo = dbEntry.getImageToImport().getPhoto();

		final Date photoUploadTime = photo.getUploadTime();

		final Date now = dateUtilsService.getCurrentDate();

		final List<String> comments = photosightPhoto.getComments();

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

	private String getUserCardFileName( final PhotosightUser photosightUser, final int page ) {
		return String.format( "userCard_%s_page_%d", photosightUser.getId(), page );
	}

	private String getPhotoCardFileName( final PhotosightPhoto photosightPhoto ) {
		return String.format( "photoCard_%d", photosightPhoto.getPhotoId() );
	}

	private User findByNameOrCreateUser( final PhotosightUser photosightUser, final PhotosightImportParameters parameters ) throws IOException {

		final String userName = getUserName( photosightUser );
		if ( StringUtils.isEmpty( userName ) ) {
			return null;
		}

		photosightUser.setName( userName );

		final String userLogin = getPhotosightUserLogin( photosightUser.getId() );
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
		user.setSelfDescription( String.format( "Photosight user: %s ( %s )", photosightUser.getId(), importParameters.getRemoteContentHelper().getUserCardUrl( photosightUser.getId(), 1 ) ) );

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

		private final List<PhotosightPhoto> photosightPhotos;
		private final boolean breakImportAfterThisPageProcessed;

		private PhotosightPhotosFromPageToImport( final List<PhotosightPhoto> photosightPhotos, final boolean breakImportAfterThisPageProcessed ) {
			this.photosightPhotos = photosightPhotos;
			this.breakImportAfterThisPageProcessed = breakImportAfterThisPageProcessed;
		}

		public List<PhotosightPhoto> getPhotosightPhotos() {
			return photosightPhotos;
		}

		public boolean isBreakImportAfterThisPageProcessed() {
			return breakImportAfterThisPageProcessed;
		}
	}
}

