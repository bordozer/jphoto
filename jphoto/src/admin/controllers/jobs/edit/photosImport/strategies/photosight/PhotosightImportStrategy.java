package admin.controllers.jobs.edit.photosImport.strategies.photosight;

import admin.jobs.entries.AbstractJob;
import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.ImageDiscEntry;
import admin.controllers.jobs.edit.photosImport.strategies.AbstractPhotoImportStrategy;
import admin.controllers.jobs.edit.photosImport.importParameters.ImportParameters;
import admin.controllers.jobs.edit.photosImport.importParameters.PhotosightImportParameters;
import admin.controllers.jobs.edit.photosImport.ImageToImport;
import core.exceptions.BaseRuntimeException;
import core.exceptions.SaveToDBException;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.log.LogHelper;
import core.services.photo.PhotoCommentService;
import core.services.security.Services;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.RandomUtilsService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import utils.*;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public class PhotosightImportStrategy extends AbstractPhotoImportStrategy {

	public static final String USERS = "users";
	public static final String PHOTOS = "photos";
	public static final String PHOTOSIGHT_USER_LOGIN_PREFIX = "PS_";

	protected PhotosightImportParameters importParameters;

	final LogHelper log = new LogHelper( PhotosightImportStrategy.class );

	private final Date firstPhotoUploadTime;

	public PhotosightImportStrategy( final AbstractJob job, final ImportParameters parameters, final Services services ) {
		super( job, services, new LogHelper( PhotosightImportStrategy.class ) );

		importParameters = ( PhotosightImportParameters ) parameters;

		firstPhotoUploadTime = getServices().getJobHelperService().getFirstPhotoUploadTime();
	}

	@Override
	public void doImport() throws IOException, SaveToDBException {

		for ( final int photosightUserId : importParameters.getPhotosightUserIds() ) {

			final PhotosightUser photosightUser = new PhotosightUser( photosightUserId );

			importPhotosightUserPhotos( photosightUser );

			log.debug( String.format( "Import photos from photosight for userId: %d", photosightUser.getId() ) );

			if( job.hasJobFinishedWithAnyResult() ) {
				break;
			}
		}
	}

	private void importPhotosightUserPhotos( final PhotosightUser photosightUser ) throws IOException, SaveToDBException {

		final User user = findByNameOrCreateUser( photosightUser, importParameters );
		if ( user == null ) {
			log.info( String.format( "User %s can not be created. Skipping photo import.", photosightUser ) );
			return;
		}

		prepareFolderForImageDownloading( photosightUser );

		final List<PhotosightPhoto> cachedLocallyPhotosightPhotos = getCachedLocallyPhotosightPhotos( photosightUser );

		int page = 1;

		final String userFirstPageContent = PhotosightRemoteContentHelper.getUserPageContent( 1, photosightUser.getId() );
		if ( StringUtils.isEmpty( userFirstPageContent ) ) {
			return; // can not load page - just skipping
		}

		final int userPagesQty = PhotosightContentHelper.getTotalPagesQty( userFirstPageContent, photosightUser.getId() );

		while ( ! job.isFinished() && ! job.hasJobFinishedWithAnyResult() && page <= userPagesQty ) {

			log.debug( String.format( "Getting page %d context of %s", page, PhotosightRemoteContentHelper.getPhotosightUserPageLink( photosightUser ) ) );

			final String userPageContent = PhotosightRemoteContentHelper.getUserPageContent( page, photosightUser.getId() );
			if ( StringUtils.isEmpty( userPageContent ) ) {
				continue; // can not load page - just skipping
			}

			final List<Integer> photosightPagePhotosIds = extractUserPhotosIds( userPageContent );

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			if ( photosightPagePhotosIds.size() == 0 ) {
				final String userCardFileName = getUserCardFileName( photosightUser, page );
				final File file = PhotosightImageFileUtils.writePageContentToFile( userCardFileName, userPageContent );
				final String message = String.format( "No photo have been found on page %d. User page content saved. See %s", page, file.getCanonicalPath() );
				job.addJobExecutionFinalMessage( message );
				log.info( message );

				continue;
			}

			final List<PhotosightPhoto> photosightPagePhotos = getPhotosightPhotosToImport( photosightUser, photosightPagePhotosIds, cachedLocallyPhotosightPhotos, user );

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

			page++;
			job.increment();
		}
	}

	private List<PhotosightPhotoOnDisk> getPhotosightPhotoOnDisk( final PhotosightUser photosightUser, final List<PhotosightPhoto> photosightPagePhotos, final List<PhotosightPhoto> cachedLocallyPhotosightPhotos ) throws IOException {
		final List<PhotosightPhotoOnDisk> photosightPhotosOnDisk = newArrayList();
		final List<PhotosightPhotoOnDisk> notCachedPhotosightPhotosOnDisk = getNotCachedPhotosightPhotosOnDisk( photosightUser, cachedLocallyPhotosightPhotos, photosightPagePhotos );
		final List<PhotosightPhotoOnDisk> cachedPhotosightPhotosOnDisk = getCachedPhotosightPhotosOnDisk( photosightPagePhotos );

		photosightPhotosOnDisk.addAll( notCachedPhotosightPhotosOnDisk );
		photosightPhotosOnDisk.addAll( cachedPhotosightPhotosOnDisk );
		return photosightPhotosOnDisk;
	}

	private List<PhotosightPhoto> getPhotosightPhotosToImport( final PhotosightUser photosightUser, final List<Integer> photosightPagePhotosIds, final List<PhotosightPhoto> cachedLocallyPhotosightPhotos, final User user ) throws IOException {
		final List<PhotosightPhoto> photosightPagePhotos = newArrayList();

		for ( final int photosightPhotoId : photosightPagePhotosIds ) {

			if ( job.hasJobFinishedWithAnyResult() ) {
				break;
			}

			if ( getServices().getJobHelperService().doesUserPhotoExist( user.getId(), photosightPhotoId ) ) {
				final String message = String.format( "Photo %d of %s has already been imported.", photosightPhotoId, PhotosightRemoteContentHelper.getPhotosightUserPageLink( photosightUser ) );
				log.debug( message );
				job.addJobExecutionFinalMessage( message );

				continue;
			}

			final PhotosightPhoto photosightPhoto;

			final PhotosightPhoto cachedPhotosightPhoto = getsPhotosightPhotoFromCache( photosightPhotoId, cachedLocallyPhotosightPhotos );
			if ( cachedPhotosightPhoto != null ) {
				photosightPhoto = cachedPhotosightPhoto;
				log.debug( String.format( "Photo %d of %s has been found in the local cache.", photosightPhotoId, PhotosightRemoteContentHelper.getPhotosightUserPageLink( photosightUser ) ) );
				job.addJobExecutionFinalMessage( String.format( "Found in the local cache: %s", PhotosightRemoteContentHelper.getPhotosightPhotoPageLink( photosightPhoto ) ) );
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

		return photosightPagePhotos;
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

	private PhotosightPhoto getsPhotosightPhotoFromCache( final int photosightPhotoId, final List<PhotosightPhoto> cachedLocallyPhotosightPhotos ) {
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

		final String photosightUserName = PhotosightRemoteContentHelper.getPhotosightUserName( photosightUser );

		if ( StringUtils.isEmpty( photosightUserName ) ) {
			log.error( String.format( "Can not extract photosight user name from page content. Photosight user: %s.", PhotosightRemoteContentHelper.getPhotosightUserPageLink( photosightUser ) ) );
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
			return PhotosightXmlUtils.getPhotosFromPhotosightUserInfoFile( photosightUser, services.getDateUtilsService() );
		} catch ( DocumentException e ) {
			final String message = String.format( "Error reading user info file: %s<br />%s", PhotosightXmlUtils.getUserInfoFile( photosightUser ), e.getMessage() );
			job.addJobExecutionFinalMessage( message );
			log.info( message );
			throw new BaseRuntimeException( e );
		}
	}

	private List<PhotosightPhotoOnDisk> downloadPhotosightPhotosOnDisk( final List<PhotosightPhoto> photosightPhotos ) throws IOException {

		final List<PhotosightPhotoOnDisk> result = newArrayList();

		for ( final PhotosightPhoto photosightPhoto : photosightPhotos ) {
			final ImageDiscEntry imageDiscEntry = PhotosightImageFileUtils.downloadPhotosightPhotoOnDisk( photosightPhoto );
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
				, dateUtilsService.formatDateTime( dateUtilsService.getCurrentTime() ), PhotosightRemoteContentHelper.getPhotosightPhotoPageLink( photosightPhoto ), genreDiscEntry.getName()
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

		final List<Integer> photosightUserIds = importParameters.getPhotosightUserIds();
		for ( final int photosightUserId : photosightUserIds ) {

			final String userPageContent = PhotosightRemoteContentHelper.getUserPageContent( 1, photosightUserId );

			if ( StringUtils.isEmpty( userPageContent ) ) {
				final String message = String.format( "ERROR getting photosight user #%d pages qty. Photos import of the user will be skipped.", photosightUserId );
				log.error( message );
				job.addJobExecutionFinalMessage( message );
				continue;
			}

			final int qty = PhotosightContentHelper.getTotalPagesQty( userPageContent, photosightUserId );
			countedTotal += qty;
			log.info( String.format( "Getting photosight user #%d pages qty: %d ( summary: %d )", photosightUserId, qty, countedTotal ) );

			if ( job.getGenerationMonitor().getStatus().isNotActive() ) {
				break;
			}
		}

		return countedTotal;
	}

	private List<Integer> extractUserPhotosIds( final String userPageContent ) {
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

		final String photoPageContent = PhotosightRemoteContentHelper.getPhotoPageContent( photosightUser, photosightPhotoId );
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
			job.addJobExecutionFinalMessage( String.format( "%s: can not get upload time from photosight photo page. Random time is used.", PhotosightRemoteContentHelper.getPhotosightPhotoPageLink( photosightPhoto ) ) );
		}

		photosightPhoto.setImageUrl( imageUrl );

		if ( importParameters.isImportComments() ) {
			final List<String> comments = PhotosightContentHelper.extractComments( photoPageContent );
			photosightPhoto.setComments( comments );
		}

		photosightPhoto.setCached( false );

		log.debug( String.format( "Photo %d has been downloaded from photosight", photosightPhoto.getPhotoId() ) );
		job.addJobExecutionFinalMessage( String.format( "Downloaded from photosight: %s of %s, photosight category: %s"
			, PhotosightRemoteContentHelper.getPhotosightPhotoPageLink( photosightPhoto )
			, PhotosightRemoteContentHelper.getPhotosightUserPageLink( photosightUser )
			, photosightPhoto.getPhotosightCategory().getName()
			)
		);

		return photosightPhoto;

	}

	private void logPhotoSkipping( final PhotosightUser photosightUser, final int photosightPhotoId, final String s ) {
		final String message = String.format( "%s User: %s; photo: %s.<br />Photo import skipped.", s, photosightUser, PhotosightRemoteContentHelper.getPhotoCardLink( photosightPhotoId ) );
		job.addJobExecutionFinalMessage( message );
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
		log.info( String.format( "Import comments for photo %d", photosightPhoto.getPhotoId() ) );

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
		return String.format( "userCard_%d_page_%d", photosightUser.getId(), page );
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

		final String userLogin = String.format( "%s%d", PHOTOSIGHT_USER_LOGIN_PREFIX, photosightUser.getId() );
		final User existingUser = services.getUserService().loadByLogin( userLogin );
		final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();
		if ( existingUser != null ) {

			job.addJobExecutionFinalMessage( TranslatorUtils.translate( String.format( "Existing user found: %s", entityLinkUtilsService.getUserCardLink( existingUser ) ) ) );

			return existingUser;
		}

		final User user = services.getFakeUserService().getRandomUser();

		user.setLogin( userLogin );
		user.setName( userName );
		user.setMembershipType( parameters.getMembershipType() );
		user.setGender( parameters.getUserGender() );
		user.setSelfDescription( String.format( "Photosight user: %d ( %s )", photosightUser.getId(), PhotosightRemoteContentHelper.getUserCardUrl( photosightUser.getId(), 1 ) ) );

		if ( ! services.getUserService().save( user ) ) {
			throw new BaseRuntimeException( "Can not create user" );
		}

		job.addJobExecutionFinalMessage( TranslatorUtils.translate( String.format( "New user has been created: %s", entityLinkUtilsService.getUserCardLink( user ) ) ) );

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
}

