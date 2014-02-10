package core.services.photo;

import core.enums.FavoriteEntryType;
import core.enums.PhotoActionAllowance;
import core.exceptions.SaveToDBException;
import core.general.base.PagingModel;
import core.general.cache.CacheEntryFactory;
import core.general.cache.CacheKey;
import core.general.cache.keys.UserGenreCompositeKey;
import core.general.configuration.ConfigurationKey;
import core.general.data.PhotoListCriterias;
import core.general.data.PhotoRating;
import core.general.data.TimeRange;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoFile;
import core.general.photo.PhotoInfo;
import core.general.photo.PhotoPreviewWrapper;
import core.general.photoTeam.PhotoTeam;
import core.general.user.User;
import core.general.user.UserPhotosByGenre;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.conversion.PhotoPreviewService;
import core.services.dao.PhotoDao;
import core.services.dao.PhotoDaoImpl;
import core.services.entry.ActivityStreamService;
import core.services.entry.EntryMenuService;
import core.services.entry.GenreService;
import core.services.security.SecurityService;
import core.services.system.CacheService;
import core.services.system.ConfigurationService;
import core.services.notification.NotificationService;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserRankService;
import core.services.user.UserService;
import core.services.user.UserTeamService;
import controllers.users.card.MarksByCategoryInfo;
import controllers.users.card.UserCardGenreInfo;
import core.services.utils.sql.BaseSqlUtilsService;
import core.services.utils.sql.PhotoCriteriasSqlService;
import core.services.utils.sql.PhotoSqlFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.SqlSelectIdsResult;
import sql.SqlSelectResult;
import sql.builder.*;
import core.services.utils.DateUtilsService;
import utils.TranslatorUtils;
import core.services.utils.UserPhotoFilePathUtilsService;
import utils.UserUtils;

import java.io.File;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.collect.Sets.newHashSet;

public class PhotoServiceImpl implements PhotoService {

	@Autowired
	private PhotoDao photoDao;

	@Autowired
	private UserService userService;

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
	private UserRankService userRankService;

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
	public void savePhotoWithTeamAndAlbums( final Photo photo, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums ) throws SaveToDBException {

		if ( ! save( photo ) ) {
			throw new SaveToDBException( String.format( "Can not save photo: %s", photo ) );
		}

		if ( ! userTeamService.savePhotoTeam( photoTeam ) ) {
			throw new SaveToDBException( String.format( "Can not save photo team: %s", photoTeam ) );
		}

		if ( ! userPhotoAlbumService.savePhotoAlbums( photo, photoAlbums ) ) {
			throw new SaveToDBException( String.format( "Can not save photo albums: %s", photoAlbums ) );
		}
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

		for ( final Integer photoId : photoIds ) {
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
	public Map<Genre, UserCardGenreInfo> getUserPhotosByGenresMap( final User user, final User votingUser ) {

		final Map<Genre, UserCardGenreInfo> photosByGenresMap = newLinkedHashMap();

		final List<Genre> genres = genreService.loadAll();

		for ( final Genre genre : genres ) {
			final UserCardGenreInfo userCardGenreInfo = getUserCardGenreInfo( votingUser, user.getId(), genre.getId() );
			if ( userCardGenreInfo.getPhotosQty() > 0 ) {
				photosByGenresMap.put( genre, userCardGenreInfo );
			}
		}

		return photosByGenresMap;
	}

	@Override
	public PhotoInfo getPhotoInfo( final Photo photo, final User accessor ) {
		final TimeRange timeRangeToday = dateUtilsService.getTimeRangeToday();

		return getPhotoInfo( photo, timeRangeToday.getTimeFrom(), timeRangeToday.getTimeTo(), accessor );
	}

	@Override
	public PhotoInfo getPhotoInfo( final Photo photo, final Date timeFrom, final Date timeTo, final User accessor ) {
		final PhotoInfo photoInfo = cacheServicePhotoInfo.getEntry( CacheKey.PHOTO_INFO, photo.getId(), new CacheEntryFactory<PhotoInfo>() {
			@Override
			public PhotoInfo createEntry() {
				return loadCachablePhotoInfoPart( photo );
			}
		} );

		updateNotCachedInPhotoInfoEntries( photo, photoInfo, timeFrom, timeTo, accessor );

		photoInfo.setPhotoTeam( userTeamService.getPhotoTeam( photo.getId() ) );

		photoInfo.setUserPhotoAlbums( userPhotoAlbumService.loadPhotoAlbums( photo.getId() ) );

		final int photoAuthorId = photo.getUserId();
		final int genreId = photo.getGenreId();
		photoInfo.setPhotoAuthorRankInGenre( userRankService.getUserRankInGenre( photoAuthorId, genreId ) );

		// TODO: duplicates UserRankServiceImpl.getVotingModel() -->
		final int minPhotosQtyForGenreRankVoting = configurationService.getInt( ConfigurationKey.RANK_VOTING_MIN_PHOTOS_QTY_IN_GENRE );
		final int userPhotosInGenre = getPhotoQtyByUserAndGenre( photoAuthorId, genreId );
		photoInfo.setPhotoAuthorHasEnoughPhotosInGenre( userPhotosInGenre >= minPhotosQtyForGenreRankVoting  );
		// TODO: duplicates UserRankServiceImpl.getVotingModel() <--

		photoInfo.setPhotoPreviewHasToBeHiddenBecauseOfNudeContent( securityService.isPhotoHasToBeHiddenBecauseOfNudeContent( photo, accessor ) );

		photoInfo.setPhotoPreviewMustBeHidden( false );

		photoInfo.setUserCanEditPhoto( securityService.userCanEditPhoto( accessor, photo ) );
		photoInfo.setUserCanDeletePhoto( securityService.userCanDeletePhoto( accessor, photo ) );

		photoInfo.setSuperAdminUser( securityService.isSuperAdminUser( accessor.getId() ) );
		photoInfo.setCommentsCount( photoCommentService.getPhotoCommentsCount( photo.getId() ) );

		photoInfo.setShowStatisticInPhotoList( configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_STATISTIC ) );
		photoInfo.setShowUserRankInGenreInPhotoList( configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_USER_RANK_IN_GENRE ) );

		return photoInfo;
	}

	@Override
	public List<PhotoInfo> getPhotoInfos( final List<Photo> photos, final User accessor ) {
		final TimeRange timeRangeToday = dateUtilsService.getTimeRangeToday();

		return getPhotoInfos( photos, timeRangeToday.getTimeFrom(), timeRangeToday.getTimeTo(), accessor );
	}

	@Override
	public List<PhotoInfo> getPhotoInfos( final List<Photo> photos, final Date timeFrom, final Date timeTo, final User accessor ) {
		final List<PhotoInfo> photoInfos = newArrayList();
		for ( Photo photo : photos ) {
			photoInfos.add( getPhotoInfo( photo, timeFrom, timeTo, accessor ) );
		}
		return photoInfos;
	}

	@Override
	public List<PhotoInfo> getPhotoInfos( final List<Photo> photos, final List<FavoriteEntryType> showIconsForFavoriteEntryTypes, final User currentUser ) {
		final List<PhotoInfo> photoInfos = getPhotoInfos( photos, currentUser );

		for ( final PhotoInfo photoInfo : photoInfos ) {
			photoInfo.setShowIconsForFavoriteEntryTypes( showIconsForFavoriteEntryTypes );
		}

		return photoInfos;
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
	public Photo getLastUserPhoto( final int userId ) {
		return photoDao.getLastUserPhoto( userId );
	}

	@Override
	public boolean isPhotoAuthorNameMustBeHidden( final Photo photo, final User accessor ) {
		final User photoAuthor = userService.load( photo.getUserId() );

		if ( securityService.isSuperAdminUser( accessor.getId() ) ) {
			return false;
		}

		boolean isPhotoOwnerWatchingThePhoto = UserUtils.isUserEqualsToCurrentUser( photoAuthor );
		if ( isPhotoOwnerWatchingThePhoto ) {
			return false;
		}

		if ( photo.isAnonymousPosting() ) {
			return dateUtilsService.getCurrentTime().getTime() < getPhotoAnonymousPeriodExpirationTime( photo ).getTime();
		}

		return false;
	}

	@Override
	public Date getPhotoAnonymousPeriodExpirationTime( final Photo photo ) {
		final int anonymousPeriod = configurationService.getConfiguration( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_PERIOD ).getValueInt();
		return dateUtilsService.getDatesOffset( photo.getUploadTime(), anonymousPeriod );
	}

	@Override
	public void hidePhotoPreviewForAnonymouslyPostedPhotos( final List<PhotoInfo> photoInfos ) {
		for ( final PhotoInfo photoInfo : photoInfos ) {
			photoInfo.setPhotoPreviewMustBeHidden( photoInfo.isPhotoAuthorNameMustBeHidden() );
		}
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
	public PhotoPreviewWrapper getPhotoPreviewWrapper( final Photo photo, final User user ) {
		final Genre genre = genreService.load( photo.getId() );
		final String photoPreviewUrl = userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo );
		final PhotoPreviewWrapper photoPreviewWrapper = new PhotoPreviewWrapper( photo, genre, photoPreviewUrl );
		photoPreviewWrapper.setPhotoPreviewHasToBeHiddenBecauseOfNudeContent( securityService.isPhotoHasToBeHiddenBecauseOfNudeContent( photo, user ) );

		return photoPreviewWrapper;
	}

	@Override
	public List<Photo> getBestUserPhotos( final User user, final int photosQty, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getUserCardUserPhotosBest( user, accessor );
		final SqlIdsSelectQuery selectQuery = photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, getPagingModel( photosQty ) );
		return loadPhotosByIdsQuery( selectQuery );
	}

	@Override
	public List<Photo> getLastUserPhotos( final User user, final int photosQty, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getUserCardUserPhotosLast( user, accessor );
		final SqlIdsSelectQuery selectQuery = photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, getPagingModel( photosQty ) );
		return loadPhotosByIdsQuery( selectQuery );
	}

	@Override
	public List<Photo> getLastVotedPhotos( final User user, final int photosQty, final User accessor ) {
		final PhotoListCriterias criterias = photoListCriteriasService.getUserCardLastVotedPhotos( user, accessor );
		final SqlIdsSelectQuery selectQuery = photoCriteriasSqlService.getForCriteriasPagedIdsSQL( criterias, getPagingModel( photosQty ) );
		return loadPhotosByIdsQuery( selectQuery );
	}

	@Override
	public List<Photo> getLastPhotosOfUserVisitors( final User user, final int photosQty ) {
		final List<Integer> userIds = photoPreviewService.getLastUsersWhoViewedUserPhotos( user.getId(), photosQty );
		final List<Photo> photos = newArrayList();
		for ( final int userId : userIds ) {
			final Photo lastUserPhoto = getLastUserPhoto( userId );
			if ( lastUserPhoto != null ) {
				photos.add( lastUserPhoto );
			}
		}

		return photos;
	}

	private UserCardGenreInfo getUserCardGenreInfo( final User votingUser, final int userId, final int genreId ) {
		final UserCardGenreInfo genreInfo = new UserCardGenreInfo();

		genreInfo.setPhotosQty( getPhotoQtyByUserAndGenre( userId, genreId ) );
		genreInfo.setVotingModel( userRankService.getVotingModel( userId, genreId, votingUser ) );

		final int userVotePointsForRankInGenre = userRankService.getUserVotePointsForRankInGenre( userId, genreId );
		genreInfo.setVotePointsForRankInGenre( userVotePointsForRankInGenre );

		genreInfo.setVotePointsToGetNextRankInGenre( userRankService.getVotePointsToGetNextRankInGenre( userVotePointsForRankInGenre ) );

		return genreInfo;
	}

	private PhotoInfo loadCachablePhotoInfoPart( final Photo photo ) {
		final int photoId = photo.getId();
		final PhotoInfo photoInfo = new PhotoInfo( photo );

		final List<MarksByCategoryInfo> marksByCategoryUser = photoVotingService.getPhotoSummaryVoicesByPhotoCategories( photo );
		photoInfo.setMarksByCategoryInfos( marksByCategoryUser );

		final int days = configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS );
		final Date dateFrom = dateUtilsService.getFirstSecondOfTheDayNDaysAgo( days );
		final Date lastSecondOfToday = dateUtilsService.getLastSecondOfToday();
		photoInfo.setTopBestMarks( photoVotingService.getPhotoMarksForPeriod( photoId, dateFrom, lastSecondOfToday ) ); // TODO: reset this at midnight

		photoInfo.setTodayMarks( photoVotingService.getPhotoMarksForPeriod( photoId, dateUtilsService.getFirstSecondOfToday(), lastSecondOfToday ) ); // TODO: reset this at midnight

		photoInfo.setTotalMarks( getSummaryPhotoMark( marksByCategoryUser ) );

		photoInfo.setPreviewCount( photoPreviewService.getPreviewCount( photoId ) );

		photoInfo.setPhotoAwards( photoAwardService.getPhotoAwards( photoId ) );

		photoInfo.setPhotoImgUrl( userPhotoFilePathUtilsService.getPhotoUrl( photo ) );
		photoInfo.setPhotoPreviewImgUrl( userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );

		return photoInfo;
	}

	private void updateNotCachedInPhotoInfoEntries( final Photo photo, final PhotoInfo photoInfo, final Date timeFrom, final Date timeTo, final User accessor ) {
		photoInfo.setPhoto( photo );

		final User user = userService.load( photo.getUserId() );
		photoInfo.setUser( user );

		final boolean isPhotoAuthorNameMustBeHidden = isPhotoAuthorNameMustBeHidden( photo, accessor );
		photoInfo.setPhotoAuthorNameMustBeHidden( isPhotoAuthorNameMustBeHidden );
		if ( isPhotoAuthorNameMustBeHidden ) {
			photoInfo.setPhotoAuthorAnonymousName( configurationService.getString( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_NAME ) );
			photoInfo.setPhotoAnonymousPeriodExpirationTime( getPhotoAnonymousPeriodExpirationTime( photo ) );
		}

		// TODO: should it be non-cachable? -->
		if ( configurationService.getBoolean( ConfigurationKey.PHOTO_LIST_SHOW_PHOTO_MENU ) ) { // TODO: no photo menu in photo card if it is switched off for photo list!!!!
			photoInfo.setPhotoMenu( entryMenuService.getPhotoMenu( photo, accessor ) );
		} else {
			photoInfo.setPhotoMenu( null );
		}

		final User photoAuthor = userService.load( photo.getUserId() );
		photoInfo.setPhotoAuthorMenu( entryMenuService.getUserMenu( photoAuthor, accessor ) );
		// TODO: should it be non-cachable? <--

		photoInfo.setGenre( genreService.load( photo.getGenreId() ) );

		if ( dateUtilsService.isNotEmptyTime( timeFrom ) && dateUtilsService.isNotEmptyTime( timeTo ) ) {
			final PhotoRating photoRatingPosition = photoRatingService.getPhotoRatingForPeriod( photo.getId(), timeFrom, timeTo );
			final boolean showPhotoRatingPosition = photoRatingPosition != null;
			photoInfo.setShowPhotoRatingPosition( showPhotoRatingPosition );
			if ( showPhotoRatingPosition ) {
				photoInfo.setPhotoRatingPosition( photoRatingPosition.getRatingPosition() );

				final String dateFrom = dateUtilsService.formatDate( timeFrom );
				final String dateTo = dateUtilsService.formatDate( timeTo );

				final boolean areDatesEquals = dateFrom.equals( dateTo );
				String dateInterval;
				final String currentDate = dateUtilsService.formatDate( dateUtilsService.getCurrentDate() );
				if ( areDatesEquals && dateFrom.equals( currentDate ) ) {
					dateInterval = "today";
				} else {
					dateInterval = areDatesEquals ? dateFrom : String.format( "%s - %s", dateFrom, dateTo );
				}

				final String photoRatingPositionDescription = TranslatorUtils.translate( "Photo's rating position on $1", dateInterval );
				photoInfo.setPhotoRatingPositionDescription( photoRatingPositionDescription );
			}
		}
	}

	private int getSummaryPhotoMark( final List<MarksByCategoryInfo> marksByCategories ) {
		int sumMark = 0;

		for ( final MarksByCategoryInfo marksByCategory : marksByCategories ) {
			sumMark += marksByCategory.getSumMark();
		}

		return sumMark;
	}

	private PagingModel getPagingModel( final int photosQty ) {
		final PagingModel pagingModel = new PagingModel();
		pagingModel.setCurrentPage( 1 );
		pagingModel.setItemsOnPage( photosQty );

		return pagingModel;
	}
}
