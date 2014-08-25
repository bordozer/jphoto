package core.services.utils.sql;

import core.enums.FavoriteEntryType;
import core.general.base.PagingModel;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.dao.*;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.*;

public class PhotoQueryServiceImpl implements PhotoQueryService {

	public static final int PORTAL_PAGE_LAST_PHOTOS_QTY = 12;

	public static final int MIN_POSSIBLE_MARK = Integer.MIN_VALUE;
	public static final String SUM_MARK_COLUMN_ALIAS = "sumMarks";

	@Autowired
	private DateUtilsService dateUtilsService;
	
	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;

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
	public SqlIdsSelectQuery getUserIdsForPageSQL( final PagingModel pagingModel ) {
		final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getUsersIdsSQL();

		baseSqlUtilsService.addUserSortByName( selectQuery );

		baseSqlUtilsService.initLimitAndOffset( selectQuery, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );

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
		baseSqlUtilsService.initLimitAndOffset( selectQuery, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage() );

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
		return new PhotoListQueryBuilder( dateUtilsService ).forPage( page, itemsOnPage ).getQuery();
	}

	public void setDateUtilsService( final DateUtilsService dateUtilsService ) {
		this.dateUtilsService = dateUtilsService;
	}

	public void setBaseSqlUtilsService( final BaseSqlUtilsService baseSqlUtilsService ) {
		this.baseSqlUtilsService = baseSqlUtilsService;
	}

}
