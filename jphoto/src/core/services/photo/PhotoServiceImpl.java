package core.services.photo;

import core.enums.PhotoActionAllowance;
import core.exceptions.SaveToDBException;
import core.general.base.PagingModel;
import core.general.cache.CacheKey;
import core.general.cache.keys.UserGenreCompositeKey;
import core.general.configuration.ConfigurationKey;
import core.general.data.PhotoListCriterias;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoFile;
import core.general.photo.PhotoInfo;
import core.general.photoTeam.PhotoTeam;
import core.general.user.User;
import core.general.user.UserPhotosByGenre;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.conversion.PhotoPreviewService;
import core.services.dao.PhotoDao;
import core.services.dao.PhotoDaoImpl;
import core.services.entry.ActivityStreamService;
import core.services.entry.GenreService;
import core.services.menu.EntryMenuService;
import core.services.notification.NotificationService;
import core.services.security.SecurityService;
import core.services.system.CacheService;
import core.services.system.ConfigurationService;
import core.services.system.Services;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserTeamService;
import core.services.utils.DateUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
import core.services.utils.sql.BaseSqlUtilsService;
import core.services.utils.sql.PhotoCriteriasSqlService;
import core.services.utils.sql.PhotoSqlFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.SqlSelectIdsResult;
import sql.SqlSelectResult;
import sql.builder.*;

import java.io.File;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

public class PhotoServiceImpl implements PhotoService {

	@Autowired
	private PhotoDao photoDao;

	@Autowired
	private GenreService genreService;

	@Autowired
	private PhotoVotingService photoVotingService;

	@Autowired
	private PhotoCommentService photoCommentService;

	@Autowired
	private CacheService<PhotoInfo> cacheServicePhotoInfo;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private PhotoPreviewService photoPreviewService;

	@Autowired
	private PhotoRatingService photoRatingService;

	@Autowired
	private PhotoAwardService photoAwardService;

	@Autowired
	private UserTeamService userTeamService;

	@Autowired
	private UserPhotoAlbumService userPhotoAlbumService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@Autowired
	private EntryMenuService entryMenuService;

	@Autowired
	private PhotoListCriteriasService photoListCriteriasService;
	
	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;
	
	@Autowired
	private PhotoSqlFilterService photoSqlFilterService;
	
	@Autowired
	private PhotoCriteriasSqlService photoCriteriasSqlService;

	@Autowired
	private ActivityStreamService activityStreamService;

	@Autowired
	private Services services;

	@Override
	public boolean save( final Photo entry ) {

		final boolean isNew = entry.isNew();

		final boolean isSaved = photoDao.saveToDB( entry );

		if ( isSaved && isNew ) {
			new Thread() {

				@Override
				public void run() {
					notificationService.newPhotoNotification( entry );
				}
			}.start();
		}

		if ( isNew ) {
			cacheService.expire( CacheKey.USER_GENRE_PHOTOS_QTY, new UserGenreCompositeKey( entry.getUserId(), entry.getGenreId() ) );
			activityStreamService.savePhotoUpload( entry );
		}

		return isSaved;
	}

	@Override
	public void uploadNewPhoto( final Photo photo, final File photoFile, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums ) throws SaveToDBException {

		if ( ! save( photo ) ) {
			throw new SaveToDBException( String.format( "Can not save photo: %s", photo ) );
		}

		if ( ! userTeamService.savePhotoTeam( photoTeam ) ) {
			throw new SaveToDBException( String.format( "Can not save photo team: %s", photoTeam ) );
		}

		if ( ! userPhotoAlbumService.savePhotoAlbums( photo, photoAlbums ) ) {
			throw new SaveToDBException( String.format( "Can not save photo albums: %s", photoAlbums ) );
		}

		/*final File photoFile = services.getUserPhotoFilePathUtilsService().copyFileToUserFolder( imageFile, photo, user );

		if ( photoService.updatePhotoFileData( photo.getId(), photoFile ) ) {
			try {
				services.getPreviewGenerationService().generatePreviewSync( photo.getId() );
			} catch ( final InterruptedException e ) {
				final String message = String.format( "Error creating preview: %s ( %s )", photoFile.getCanonicalPath(), e.getMessage() );

				log.error( message );

				photo.setDescription( message );
				photoService.save( photo ); // SAVE ERROR TO DESCRIPTION
				// TODO: delete photo from DB because is has no file
			}
		}*/
	}

	@Override
	public Photo load( final int photoId ) {
		return photoDao.load( photoId );
	}

	@Override
	public boolean delete( final int entryId ) {

		final Photo photo = load( entryId ); // Must be loaded before photo photoDao.delete()

		photoPreviewService.deletePreviews( entryId );

		photoCommentService.deletePhotoComments( entryId );

		photoVotingService.deletePhotoVoting( entryId );

		photoAwardService.deletePhotoAwards( entryId );

		photoRatingService.deletePhotoRatings( entryId );

		userTeamService.deletePhotoTeam( entryId );

		userPhotoAlbumService.deletePhotoFromAllAlbums( entryId );

		final boolean isDeleted = photoDao.delete( entryId );

		if ( isDeleted ) {
			userPhotoFilePathUtilsService.deletePhotoFileWithPreview( photo );

			cacheService.expire( CacheKey.PHOTO, entryId );
			cacheService.expire( CacheKey.PHOTO_INFO, entryId );
			cacheService.expire( CacheKey.USER_GENRE_PHOTOS_QTY, new UserGenreCompositeKey( photo.getUserId(), photo.getGenreId() ) );
		}

		return isDeleted;
	}

	@Override
	public SqlSelectIdsResult load( final SqlIdsSelectQuery selectIdsQuery ) {
		return photoDao.load( selectIdsQuery );
	}

	@Override
	public List<Photo> load( final List<Integer> photoIds ) {
		final List<Photo> result = newArrayList();

		for ( final int photoId : photoIds ) {
			result.add( load( photoId ) );
		}

		return result;
	}

	@Deprecated
	@Override
	public SqlSelectResult<Photo> load( final SqlSelectQuery selectQuery ) { // TODO: load from cache
		return photoDao.load( selectQuery );
	}

	@Override
	public int getPhotoQty() {
		return photoDao.getPhotoQty();
	}

	@Override
	public int getPhotoQtyByGenre( final int genreId ) {
		return photoDao.getPhotoQtyByGenre( genreId );
	}

	@Override
	public int getPhotoQtyByUser( final int userId ) {
		return photoDao.getPhotoQtyByUser( userId );
	}

	@Override
	public List<Photo> getUserPhotos( final int userId ) {
		return photoDao.getUserPhotos( userId );
	}

	@Override
	public List<Integer> getUserPhotosIds( final int userId ) {
		return photoDao.getUserPhotosIds( userId );
	}

	@Override
	public int getPhotoQtyByUserAndGenre( final int userId, final int genreId ) {
		return photoDao.getPhotoQtyByUserAndGenre( userId, genreId );
	}

	@Override
	public Set<Genre> getUserPhotoGenres( final int userId ) {
		final Set<Genre> result = newHashSet();

		final List<Genre> genres = genreService.loadAll();
		for ( final Genre genre : genres ) {
			final int photosInGenre = getPhotoQtyByUserAndGenre( userId, genre.getId() );
			if ( photosInGenre > 0 ) {
				result.add( genre );
			}
		}

		return result;
	}

	@Override
	public List<Photo> loadPhotosByIdsQuery( final SqlIdsSelectQuery selectIdsQuery ) {
		final SqlSelectIdsResult sqlSelectIdsResult = load( selectIdsQuery );
		return load( sqlSelectIdsResult.getIds() );
	}

	@Override
	public List<Photo> loadUserPhotos( final int userId ) {
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getPhotosIdsSQL();

		final SqlTable tPhoto = selectQuery.getMainTable();
		final SqlColumnSelectable tPhotoColUserId = new SqlColumnSelect( tPhoto, PhotoDaoImpl.TABLE_COLUMN_USER_ID );
		final SqlLogicallyJoinable condition = new SqlCondition( tPhotoColUserId, SqlCriteriaOperator.EQUALS, userId, dateUtilsService );
		selectQuery.setWhere( condition );

		final List<Integer> photoIds = load( selectQuery ).getIds();
		return load( photoIds );
	}



	@Override
	public boolean updatePhotoFileData( final int photoId, final File file ) {
		final PhotoFile photoFile = new PhotoFile( file );

		final boolean result = photoDao.updatePhotoFile( photoId, photoFile );

		if ( result ) {
			cacheServicePhotoInfo.expire( CacheKey.PHOTO_INFO, photoId );
		}

		return result;
	}

	@Override
	public int getPhotoQtyByGenreForPeriod( final int genreId, final Date timeFrom, final Date timeTo ) {
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getPhotosIdsSQL();

		photoSqlFilterService.addFilterByGenre( genreId, selectQuery );
		photoCriteriasSqlService.addUploadTimeCriteria( timeFrom, timeTo, selectQuery );

		final SqlSelectIdsResult idsResult = load( selectQuery );

		return idsResult.getRecordQty();
	}

	@Override
	public int getLastUserPhotoId( final int userId ) {
		return photoDao.getLastUserPhotoId( userId );
	}

	@Override
	public Date getPhotoAnonymousPeriodExpirationTime( final Photo photo ) {
		final int anonymousPeriod = configurationService.getConfiguration( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_PERIOD ).getValueInt();
		return dateUtilsService.getDatesOffset( photo.getUploadTime(), anonymousPeriod );
	}

	@Override
	public PhotoActionAllowance getPhotoCommentAllowance( final Photo photo ) {
		final PhotoActionAllowance allowance = photo.getCommentsAllowance();

		if ( allowance == PhotoActionAllowance.CANDIDATES_AND_MEMBERS && ! configurationService.getBoolean( ConfigurationKey.CANDIDATES_CAN_COMMENT_PHOTOS ) ) {
			return PhotoActionAllowance.MEMBERS_ONLY;
		}

		return allowance;
	}

	@Override
	public PhotoActionAllowance getPhotoVotingAllowance( final Photo photo ) {
		final PhotoActionAllowance allowance = photo.getVotingAllowance();

		if ( allowance == PhotoActionAllowance.CANDIDATES_AND_MEMBERS && ! configurationService.getBoolean( ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_PHOTOS ) ) {
			return PhotoActionAllowance.MEMBERS_ONLY;
		}

		return allowance;
	}

	@Override
	public boolean exists( final int entryId ) {
		return photoDao.exists( entryId );
	}

	@Override
	public boolean exists( final Photo entry ) {
		return photoDao.exists( entry );
	}

	@Override
	public List<UserPhotosByGenre> getUserPhotosByGenres( final int userId ) {
		final Set<Genre> genres = getUserPhotoGenres( userId );
		final List<UserPhotosByGenre> userPhotosByGenres = newArrayList();
		for ( final Genre genre : genres ) {
			final UserPhotosByGenre photosByGenre = new UserPhotosByGenre( genre );
			final int photoQtyByUserAndGenre = getPhotoQtyByUserAndGenre( userId, genre.getId() );
			photosByGenre.setPhotosQty( photoQtyByUserAndGenre );

			userPhotosByGenres.add( photosByGenre );
		}

		Collections.sort( userPhotosByGenres, new Comparator<UserPhotosByGenre>() {
			@Override
			public int compare( final UserPhotosByGenre o1, final UserPhotosByGenre o2 ) {
				return o1.getGenre().getName().compareTo( o2.getGenre().getName() );
			}
		} );

		return userPhotosByGenres;
	}

	@Override
	public List<Integer> getBestUserPhotosIds( final User user, final int photosQty, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getUserCardUserPhotosBest( user, accessor );
		final SqlIdsSelectQuery selectQuery = photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, getPagingModel( photosQty ) );

		return load( selectQuery ).getIds();
	}

	@Override
	public List<Integer> getLastUserPhotosIds( final User user, final int photosQty, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getUserCardUserPhotosLast( user, accessor );
		final SqlIdsSelectQuery selectQuery = photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, getPagingModel( photosQty ) );

		return load( selectQuery ).getIds();
	}

	@Override
	public List<Integer> getLastVotedPhotosIds( final User user, final int photosQty, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getUserCardLastVotedPhotos( user, accessor );
		final SqlIdsSelectQuery selectQuery = photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, getPagingModel( photosQty ) );

		return load( selectQuery ).getIds();
	}

	@Override
	public List<Integer> getLastPhotosOfUserVisitors( final User user, final int photosQty ) {
		final List<Integer> userIds = photoPreviewService.getLastUsersWhoViewedUserPhotos( user.getId(), photosQty );
		final List<Integer> photosIds = newArrayList();
		for ( final int userId : userIds ) {
			final int lastUserPhotoId = getLastUserPhotoId( userId );
			if ( lastUserPhotoId > 0 ) {
				photosIds.add( lastUserPhotoId );
			}
		}

		return photosIds;
	}

	private PagingModel getPagingModel( final int photosQty ) {
		final PagingModel pagingModel = new PagingModel( services );
		pagingModel.setCurrentPage( 1 );
		pagingModel.setItemsOnPage( photosQty );

		return pagingModel;
	}
}
