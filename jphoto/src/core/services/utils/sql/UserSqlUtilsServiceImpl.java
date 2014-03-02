package core.services.utils.sql;

import core.enums.FavoriteEntryType;
import core.general.base.PagingModel;
import core.general.user.UserMembershipType;
import core.services.dao.FavoritesDaoImpl;
import core.services.dao.UserDaoImpl;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.builder.*;

public class UserSqlUtilsServiceImpl implements UserSqlUtilsService {

	@Autowired
	private DateUtilsService dateUtilsService;
	
	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;

	@Override
	public SqlSelectQuery getAllUsersForPageSQL( final PagingModel pagingModel ) {
		final SqlSelectQuery selectQuery = baseSqlUtilsService.getUsersSQL();

		baseSqlUtilsService.initLimitAndOffset( selectQuery, pagingModel );

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
		selectQuery.addWhereAnd( new SqlCondition( tFavoritesColFavEntryType, SqlCriteriaOperator.EQUALS, FavoriteEntryType.USER.getId(), dateUtilsService ) );

		return selectQuery;
	}
}
