package core.services.utils.sql;

import core.enums.FavoriteEntryType;
import core.general.base.PagingModel;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.services.dao.*;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.*;

import java.util.Date;

public class PhotoSqlHelperServiceImpl implements PhotoSqlHelperService {

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
	public SqlIdsSelectQuery getAllPhotosForPageIdsSQL( final PagingModel pagingModel ) {
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getPhotosIdsSQL();
		baseSqlUtilsService.initLimitAndOffset( selectQuery, pagingModel );

		return selectQuery;
	}

	@Override
	public SqlIdsSelectQuery getAllPhotosBestIdsSQL( final int minMarks, final Date timeFrom, final Date timeTo ) {
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getPhotosIdsSQL();

		photoSqlFilterService.addJoinWithPhotoVotingTable( selectQuery );

		photoSqlFilterService.addFilterByMinVotedMark( selectQuery, minMarks );

		photoSqlFilterService.addFilterForVotingTimeForLastNDays( selectQuery, timeFrom, timeTo );

		return selectQuery;
	}

	// Portal Page -->
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
	public SqlIdsSelectQuery getPortalPageBestPhotosIdsSQL( final int minMarksToBeInPhotoOfTheDay, final Date timeFrom, final Date timeTo ) {
		final SqlIdsSelectQuery selectQuery = getAllPhotosBestIdsSQL( minMarksToBeInPhotoOfTheDay, timeFrom, timeTo );
		selectQuery.setLimit( PORTAL_PAGE_BEST_PHOTOS_QTY );

		baseSqlUtilsService.addSortBySumVotingMarksDesc( selectQuery );

		return selectQuery;
	}
	// Portal Page <--

	@Override
	public SqlIdsSelectQuery getFavoritesPhotosSQL( final PagingModel pagingModel, final int userId, final FavoriteEntryType entryType ) {
		final SqlIdsSelectQuery selectQuery = getAllPhotosForPageIdsSQL( pagingModel );

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
	public SqlIdsSelectQuery getPhotosOfUserFavoritesMembersSQL( final PagingModel pagingModel, final int userId ) {
		final SqlIdsSelectQuery selectQuery = getAllPhotosForPageIdsSQL( pagingModel );

		final SqlTable tPhotos = selectQuery.getMainTable();
		final SqlTable tFavor = new SqlTable( FavoritesDaoImpl.TABLE_FAVORITES );

		final SqlColumnSelect tPhotosColUserId = new SqlColumnSelect( tPhotos, PhotoDaoImpl.TABLE_COLUMN_USER_ID );
		final SqlColumnSelect tFavColEntryId = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_FAVORITE_ENTRY_ID );
		final SqlJoinCondition joinCondition = new SqlJoinCondition( tPhotosColUserId, tFavColEntryId );
		final SqlJoin join = SqlJoin.inner( tFavor, joinCondition );
		selectQuery.joinTable( join );

		final SqlColumnSelect tFavColEntryUserId = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_USER_ID );
		final SqlColumnSelect tFavColEntryType = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_ENTRY_TYPE );
		final SqlLogicallyJoinable con1 = new SqlCondition( tFavColEntryUserId, SqlCriteriaOperator.EQUALS, userId, dateUtilsService );
		final SqlLogicallyJoinable con2 = new SqlCondition( tFavColEntryType, SqlCriteriaOperator.EQUALS, FavoriteEntryType.FAVORITE_MEMBERS.getId(), dateUtilsService );
		final SqlLogicallyJoinable condList = new SqlLogicallyAnd( con1, con2 );
		selectQuery.setWhere( condList );

		final SqlColumnSelect column = new SqlColumnSelect( tFavor, FavoritesDaoImpl.TABLE_COLUMN_CREATED );
		selectQuery.addSortingDesc( column );

		return selectQuery;
	}

	@Override
	public SqlIdsSelectQuery getUserTeamMemberPhotosQuery( final User user, final UserTeamMember userTeamMember, final int page, final int itemsOnPage ) {
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getPhotosIdsSQL();
		baseSqlUtilsService.initLimitAndOffset( selectQuery, page, itemsOnPage );

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
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getPhotosIdsSQL();
		baseSqlUtilsService.initLimitAndOffset( selectQuery, page, itemsOnPage );

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
}
