package core.services.utils.sql;

import core.enums.FavoriteEntryType;
import core.general.base.PagingModel;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.services.dao.*;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.*;

import java.util.Date;

public class PhotoQueryServiceImpl implements PhotoQueryService {

	public static final int PORTAL_PAGE_LAST_PHOTOS_QTY = 12;
	public static final int PORTAL_PAGE_BEST_PHOTOS_QTY = 8;

	public static final int MIN_MARK_FOR_BEST = 1;
	public static final int MIN_POSSIBLE_MARK = Integer.MIN_VALUE;
	public static final String SUM_MARK_COLUMN_ALIAS = "sumMarks";

	@Autowired
	private DateUtilsService dateUtilsService;
	
	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;
	
	@Autowired
	private PhotoSqlFilterService photoSqlFilterService;

	@Override
	public SqlIdsSelectQuery getPortalPageLastUploadedPhotosSQL() {
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getPhotosIdsSQL();

		final SqlColumnSelect column = new SqlColumnSelect( selectQuery.getMainTable(), PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME );
		selectQuery.addSortingDesc( column );
		selectQuery.setLimit( PORTAL_PAGE_LAST_PHOTOS_QTY );

		baseSqlUtilsService.addDescSortByUploadTimeDesc( selectQuery );

		return selectQuery;
	}

	@Override
	public SqlIdsSelectQuery getPortalPageBestPhotosIdsSQL( final int minMarksToBeInPhotoOfTheDay, final Date timeFrom ) {
		return getPortalPageBestPhotosIdsSQL( minMarksToBeInPhotoOfTheDay, timeFrom, dateUtilsService.getLastSecondOfDay( dateUtilsService.getCurrentTime() ) );
	}

	@Override
	public SqlIdsSelectQuery getFavoritesPhotosSQL( final int userId, final FavoriteEntryType entryType, final int page, final int itemsOnPage ) {
		final SqlIdsSelectQuery selectQuery = getQuery( page, itemsOnPage );

		final SqlTable tPhotos = selectQuery.getMainTable();
		final SqlTable tFavor = new SqlTable( FavoritesDaoImpl.TABLE_FAVORITES );

		final SqlColumnSelect tPhotosColId = new SqlColumnSelect( tPhotos, BaseEntityDao.ENTITY_ID );
		final SqlColumnSelect tFavColEntryId = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_FAVORITE_ENTRY_ID );
		final SqlJoinCondition joinCondition = new SqlJoinCondition( tPhotosColId, tFavColEntryId );
		final SqlJoin join = SqlJoin.inner( tFavor, joinCondition );
		selectQuery.joinTable( join );

		final SqlColumnSelect tFavColEntryUserId = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_USER_ID );
		final SqlColumnSelect tFavColEntryType = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_ENTRY_TYPE );
		final SqlLogicallyJoinable con1 = new SqlCondition( tFavColEntryUserId, SqlCriteriaOperator.EQUALS, userId, dateUtilsService );
		final SqlLogicallyJoinable con2 = new SqlCondition( tFavColEntryType, SqlCriteriaOperator.EQUALS, entryType.getId(), dateUtilsService );
		final SqlLogicallyJoinable condList = new SqlLogicallyAnd( con1, con2 );
		selectQuery.setWhere( condList );

		final SqlColumnSelect column = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_CREATED );
		selectQuery.addSortingDesc( column );

		return selectQuery;
	}

	@Override
	public SqlIdsSelectQuery getPhotosOfUserFavoritesMembersSQL( final User user, final int page, final int itemsOnPage ) {
		final SqlIdsSelectQuery selectQuery = getQuery( page, itemsOnPage );

		final SqlTable tPhotos = selectQuery.getMainTable();
		final SqlTable tFavor = new SqlTable( FavoritesDaoImpl.TABLE_FAVORITES );

		final SqlColumnSelect tPhotosColUserId = new SqlColumnSelect( tPhotos, PhotoDaoImpl.TABLE_COLUMN_USER_ID );
		final SqlColumnSelect tFavColEntryId = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_FAVORITE_ENTRY_ID );
		final SqlJoinCondition joinCondition = new SqlJoinCondition( tPhotosColUserId, tFavColEntryId );
		final SqlJoin join = SqlJoin.inner( tFavor, joinCondition );
		selectQuery.joinTable( join );

		final SqlColumnSelect tFavColEntryUserId = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_USER_ID );
		final SqlColumnSelect tFavColEntryType = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_ENTRY_TYPE );
		final SqlLogicallyJoinable con1 = new SqlCondition( tFavColEntryUserId, SqlCriteriaOperator.EQUALS, user.getId(), dateUtilsService );
		final SqlLogicallyJoinable con2 = new SqlCondition( tFavColEntryType, SqlCriteriaOperator.EQUALS, FavoriteEntryType.FAVORITE_MEMBERS.getId(), dateUtilsService );
		final SqlLogicallyJoinable condList = new SqlLogicallyAnd( con1, con2 );
		selectQuery.setWhere( condList );

		final SqlColumnSelect column = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_CREATED );
		selectQuery.addSortingDesc( column );

		return selectQuery;
	}

	@Override
	public SqlIdsSelectQuery getUserTeamMemberPhotosQuery( final User user, final UserTeamMember userTeamMember, final int page, final int itemsOnPage ) {
		final SqlIdsSelectQuery selectQuery = getQuery( page, itemsOnPage );

		photoSqlFilterService.addFilterByUser( user.getId(), selectQuery );

		final SqlTable tPhoto = selectQuery.getMainTable();
		final SqlTable tPhotoTeam = new SqlTable( UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM );

		final SqlColumnSelect tPhotoColId = new SqlColumnSelect( tPhoto, BaseEntityDao.ENTITY_ID );
		final SqlColumnSelect tPhotoTeamColPhotoId = new SqlColumnSelect( tPhotoTeam, UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM_COL_PHOTO_ID );

		final SqlJoin join = SqlJoin.leftOuter( tPhotoTeam, new SqlJoinCondition( tPhotoColId, tPhotoTeamColPhotoId ) );
		selectQuery.joinTable( join );

		final SqlColumnSelectable tPhotoTeamColUserTeamMemberId = new SqlColumnSelect( tPhotoTeam, UserTeamMemberDaoImpl.TABLE_PHOTO_TEAM_COL_USER_TEAM_MEMBER_ID );
		final SqlLogicallyJoinable condition = new SqlCondition( tPhotoTeamColUserTeamMemberId, SqlCriteriaOperator.EQUALS, userTeamMember.getId(), dateUtilsService );
		selectQuery.addWhereAnd( condition );

		baseSqlUtilsService.addDescSortByUploadTimeDesc( selectQuery );

		return selectQuery;
	}

	@Override
	public SqlIdsSelectQuery getUserPhotoAlbumPhotosQuery( final User user, final UserPhotoAlbum userPhotoAlbum, final int page, final int itemsOnPage ) {
		final SqlIdsSelectQuery selectQuery = getQuery( page, itemsOnPage );

		photoSqlFilterService.addFilterByUser( user.getId(), selectQuery );

		final SqlTable tPhoto = selectQuery.getMainTable();
		final SqlTable tUserPhotoAlbumPhotos = new SqlTable( UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS );

		final SqlColumnSelect tPhotoColId = new SqlColumnSelect( tPhoto, BaseEntityDao.ENTITY_ID );
		final SqlColumnSelect tUserPhotoAlbumColPhotoId = new SqlColumnSelect( tUserPhotoAlbumPhotos, UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS_COL_PHOTO_ID );

		final SqlJoin join = SqlJoin.leftOuter( tUserPhotoAlbumPhotos, new SqlJoinCondition( tPhotoColId, tUserPhotoAlbumColPhotoId ) );
		selectQuery.joinTable( join );

		final SqlColumnSelectable tUserPhotoAlbumColAlbumId = new SqlColumnSelect( tUserPhotoAlbumPhotos, UserPhotoAlbumDaoImpl.TABLE_PHOTO_ALBUMS_COL_ALBUM_ID );
		final SqlLogicallyJoinable condition = new SqlCondition( tUserPhotoAlbumColAlbumId, SqlCriteriaOperator.EQUALS, userPhotoAlbum.getId(), dateUtilsService );
		selectQuery.addWhereAnd( condition );

		baseSqlUtilsService.addDescSortByUploadTimeDesc( selectQuery );

		return selectQuery;
	}

	@Override
	public SqlIdsSelectQuery getUserIdsForPageSQL( final PagingModel pagingModel ) {
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getUsersIdsSQL();

		baseSqlUtilsService.addUserSortByName( selectQuery );

		baseSqlUtilsService.initLimitAndOffset( selectQuery, pagingModel );

		return selectQuery;
	}

	@Override
	public SqlIdsSelectQuery getUsersByMembershipTypeSQL( final UserMembershipType membershipType, final PagingModel pagingModel ) {

		final SqlIdsSelectQuery selectIdsQuery = getUserIdsForPageSQL( pagingModel );
		final SqlColumnSelect tUserColMemberType = new SqlColumnSelect( selectIdsQuery.getMainTable(), UserDaoImpl.TABLE_COLUMN_MEMBERSHIP_TYPE );
		final SqlLogicallyJoinable condition = new SqlCondition( tUserColMemberType, SqlCriteriaOperator.EQUALS, membershipType.getId(), dateUtilsService );
		selectIdsQuery.addWhereAnd( condition );

		return selectIdsQuery;
	}

	@Override
	public SqlIdsSelectQuery getUsersFavoritesSQL( final PagingModel pagingModel, final int userId, final FavoriteEntryType entryType ) {

		final SqlIdsSelectQuery selectQuery = getUserIdsForPageSQL( pagingModel );

		final SqlTable tUser = selectQuery.getMainTable();
		final SqlTable tFavorites = new SqlTable( FavoritesDaoImpl.TABLE_FAVORITES );

		final SqlColumnSelect tUserColId = new SqlColumnSelect( tUser, UserDaoImpl.ENTITY_ID );
		final SqlColumnSelect tFavoritesColUserId = new SqlColumnSelect( tFavorites, FavoritesDaoImpl.TABLE_COLUMN_USER_ID );
		final SqlColumnSelect tFavoritesColFavEntryType = new SqlColumnSelect( tFavorites, FavoritesDaoImpl.TABLE_COLUMN_ENTRY_TYPE );
		final SqlColumnSelect tFavoritesColFavEntryId = new SqlColumnSelect( tFavorites, FavoritesDaoImpl.TABLE_COLUMN_FAVORITE_ENTRY_ID );

		final SqlJoin join = SqlJoin.inner( tFavorites, new SqlJoinCondition( tUserColId, tFavoritesColFavEntryId ) );
		selectQuery.joinTable( join );

		selectQuery.addWhereAnd( new SqlCondition( tFavoritesColUserId, SqlCriteriaOperator.EQUALS, userId, dateUtilsService ) );
		selectQuery.addWhereAnd( new SqlCondition( tFavoritesColFavEntryType, SqlCriteriaOperator.EQUALS, entryType.getId(), dateUtilsService ) );

		return selectQuery;
	}

	@Override
	public SqlIdsSelectQuery getAddedToFavoritesBySQL( final PagingModel pagingModel, final int userId ) {
		final SqlTable tFavorites = new SqlTable( FavoritesDaoImpl.TABLE_FAVORITES );
		final SqlColumnSelect tFavoritesColUserId = new SqlColumnSelect( tFavorites, FavoritesDaoImpl.TABLE_COLUMN_USER_ID );

		final SqlIdsSelectQuery selectQuery = new SqlIdsSelectQuery( new SqlTable( UserDaoImpl.TABLE_USERS ), tFavoritesColUserId );
		baseSqlUtilsService.initLimitAndOffset( selectQuery, pagingModel );

		final SqlTable tUser = selectQuery.getMainTable();


		final SqlColumnSelect tUserColId = new SqlColumnSelect( tUser, UserDaoImpl.ENTITY_ID );

		final SqlColumnSelect tFavoritesColFavEntryType = new SqlColumnSelect( tFavorites, FavoritesDaoImpl.TABLE_COLUMN_ENTRY_TYPE );
		final SqlColumnSelect tFavoritesColFavEntryId = new SqlColumnSelect( tFavorites, FavoritesDaoImpl.TABLE_COLUMN_FAVORITE_ENTRY_ID );

		final SqlJoin join = SqlJoin.inner( tFavorites, new SqlJoinCondition( tUserColId, tFavoritesColFavEntryId ) );
		selectQuery.joinTable( join );

		selectQuery.addWhereAnd( new SqlCondition( tFavoritesColFavEntryId, SqlCriteriaOperator.EQUALS, userId, dateUtilsService ) );
		selectQuery.addWhereAnd( new SqlCondition( tFavoritesColFavEntryType, SqlCriteriaOperator.EQUALS, FavoriteEntryType.FAVORITE_MEMBERS.getId(), dateUtilsService ) );

		return selectQuery;
	}

	private SqlIdsSelectQuery getQuery( final int page, final int itemsOnPage ) {
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getPhotosIdsSQL();
		baseSqlUtilsService.initLimitAndOffset( selectQuery, page, itemsOnPage );
		return selectQuery;
	}

	private SqlIdsSelectQuery getAllPhotosBestIdsSQL( final int minMarks, final Date timeFrom, final Date timeTo ) {
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getPhotosIdsSQL();

		photoSqlFilterService.addJoinWithPhotoVotingTable( selectQuery );

		photoSqlFilterService.addFilterByMinVotedMark( selectQuery, minMarks );

		photoSqlFilterService.addFilterForVotingTimeForLastNDays( selectQuery, timeFrom, timeTo );

		return selectQuery;
	}

	private SqlIdsSelectQuery getPortalPageBestPhotosIdsSQL( final int minMarksToBeInPhotoOfTheDay, final Date timeFrom, final Date timeTo ) {
		final SqlIdsSelectQuery selectQuery = getAllPhotosBestIdsSQL( minMarksToBeInPhotoOfTheDay, timeFrom, timeTo );
		selectQuery.setLimit( PORTAL_PAGE_BEST_PHOTOS_QTY );

		baseSqlUtilsService.addSortBySumVotingMarksDesc( selectQuery );

		return selectQuery;
	}

	public void setDateUtilsService( final DateUtilsService dateUtilsService ) {
		this.dateUtilsService = dateUtilsService;
	}

	public void setBaseSqlUtilsService( final BaseSqlUtilsService baseSqlUtilsService ) {
		this.baseSqlUtilsService = baseSqlUtilsService;
	}

	public void setPhotoSqlFilterService( final PhotoSqlFilterService photoSqlFilterService ) {
		this.photoSqlFilterService = photoSqlFilterService;
	}
}
