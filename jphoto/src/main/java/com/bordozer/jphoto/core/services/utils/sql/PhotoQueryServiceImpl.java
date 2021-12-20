package com.bordozer.jphoto.core.services.utils.sql;

import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.general.base.PagingModel;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.services.dao.FavoritesDaoImpl;
import com.bordozer.jphoto.core.services.dao.UserDaoImpl;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.sql.builder.SqlColumnSelect;
import com.bordozer.jphoto.sql.builder.SqlCondition;
import com.bordozer.jphoto.sql.builder.SqlCriteriaOperator;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;
import com.bordozer.jphoto.sql.builder.SqlJoin;
import com.bordozer.jphoto.sql.builder.SqlJoinCondition;
import com.bordozer.jphoto.sql.builder.SqlLogicallyJoinable;
import com.bordozer.jphoto.sql.builder.SqlTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("photoQueryService")
public class PhotoQueryServiceImpl implements PhotoQueryService {

    public static final int MIN_POSSIBLE_MARK = Integer.MIN_VALUE;
    public static final String SUM_MARK_COLUMN_ALIAS = "sumMarks";

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private BaseSqlUtilsService baseSqlUtilsService;

    @Override
    public SqlIdsSelectQuery getUserIdsForPageSQL(final PagingModel pagingModel) {
        final SqlIdsSelectQuery selectQuery = baseSqlUtilsService.getUsersIdsSQL();

        baseSqlUtilsService.addUserSortByName(selectQuery);

        baseSqlUtilsService.initLimitAndOffset(selectQuery, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage());

        return selectQuery;
    }

    @Override
    public SqlIdsSelectQuery getUsersByMembershipTypeSQL(final UserMembershipType membershipType, final PagingModel pagingModel) {

        final SqlIdsSelectQuery selectIdsQuery = getUserIdsForPageSQL(pagingModel);
        final SqlColumnSelect tUserColMemberType = new SqlColumnSelect(selectIdsQuery.getMainTable(), UserDaoImpl.TABLE_COLUMN_MEMBERSHIP_TYPE);
        final SqlLogicallyJoinable condition = new SqlCondition(tUserColMemberType, SqlCriteriaOperator.EQUALS, membershipType.getId(), dateUtilsService);
        selectIdsQuery.addWhereAnd(condition);

        return selectIdsQuery;
    }

    @Override
    public SqlIdsSelectQuery getUsersFavoritesSQL(final PagingModel pagingModel, final int userId, final FavoriteEntryType entryType) {

        final SqlIdsSelectQuery selectQuery = getUserIdsForPageSQL(pagingModel);

        final SqlTable tUser = selectQuery.getMainTable();
        final SqlTable tFavorites = new SqlTable(FavoritesDaoImpl.TABLE_FAVORITES);

        final SqlColumnSelect tUserColId = new SqlColumnSelect(tUser, UserDaoImpl.ENTITY_ID);
        final SqlColumnSelect tFavoritesColUserId = new SqlColumnSelect(tFavorites, FavoritesDaoImpl.TABLE_COLUMN_USER_ID);
        final SqlColumnSelect tFavoritesColFavEntryType = new SqlColumnSelect(tFavorites, FavoritesDaoImpl.TABLE_COLUMN_ENTRY_TYPE);
        final SqlColumnSelect tFavoritesColFavEntryId = new SqlColumnSelect(tFavorites, FavoritesDaoImpl.TABLE_COLUMN_FAVORITE_ENTRY_ID);

        final SqlJoin join = SqlJoin.inner(tFavorites, new SqlJoinCondition(tUserColId, tFavoritesColFavEntryId));
        selectQuery.joinTable(join);

        selectQuery.addWhereAnd(new SqlCondition(tFavoritesColUserId, SqlCriteriaOperator.EQUALS, userId, dateUtilsService));
        selectQuery.addWhereAnd(new SqlCondition(tFavoritesColFavEntryType, SqlCriteriaOperator.EQUALS, entryType.getId(), dateUtilsService));

        return selectQuery;
    }

    @Override
    public SqlIdsSelectQuery getAddedToFavoritesBySQL(final PagingModel pagingModel, final int userId) {
        final SqlTable tFavorites = new SqlTable(FavoritesDaoImpl.TABLE_FAVORITES);
        final SqlColumnSelect tFavoritesColUserId = new SqlColumnSelect(tFavorites, FavoritesDaoImpl.TABLE_COLUMN_USER_ID);

        final SqlIdsSelectQuery selectQuery = new SqlIdsSelectQuery(new SqlTable(UserDaoImpl.TABLE_USERS), tFavoritesColUserId);
        baseSqlUtilsService.initLimitAndOffset(selectQuery, pagingModel.getCurrentPage(), pagingModel.getItemsOnPage());

        final SqlTable tUser = selectQuery.getMainTable();


        final SqlColumnSelect tUserColId = new SqlColumnSelect(tUser, UserDaoImpl.ENTITY_ID);

        final SqlColumnSelect tFavoritesColFavEntryType = new SqlColumnSelect(tFavorites, FavoritesDaoImpl.TABLE_COLUMN_ENTRY_TYPE);
        final SqlColumnSelect tFavoritesColFavEntryId = new SqlColumnSelect(tFavorites, FavoritesDaoImpl.TABLE_COLUMN_FAVORITE_ENTRY_ID);

        final SqlJoin join = SqlJoin.inner(tFavorites, new SqlJoinCondition(tUserColId, tFavoritesColFavEntryId));
        selectQuery.joinTable(join);

        selectQuery.addWhereAnd(new SqlCondition(tFavoritesColFavEntryId, SqlCriteriaOperator.EQUALS, userId, dateUtilsService));
        selectQuery.addWhereAnd(new SqlCondition(tFavoritesColFavEntryType, SqlCriteriaOperator.EQUALS, FavoriteEntryType.FAVORITE_MEMBERS.getId(), dateUtilsService));

        return selectQuery;
    }

    public void setDateUtilsService(final DateUtilsService dateUtilsService) {
        this.dateUtilsService = dateUtilsService;
    }

    public void setBaseSqlUtilsService(final BaseSqlUtilsService baseSqlUtilsService) {
        this.baseSqlUtilsService = baseSqlUtilsService;
    }

}
