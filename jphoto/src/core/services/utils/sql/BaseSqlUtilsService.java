package core.services.utils.sql;

import core.general.base.PagingModel;
import sql.builder.*;

public interface BaseSqlUtilsService {

	void initLimitAndOffset( final BaseSqlSelectQuery sqlSelectQuery, final PagingModel pagingModel );

	void initLimitAndOffset( final BaseSqlSelectQuery sqlSelectQuery, final int page, final int itemsOnPage );

	SqlSelectQuery getSqlSelectQuery( final String table );

	SqlIdsSelectQuery getPhotosIdsSQL();

	SqlSelectQuery getUsersSQL();

	SqlIdsSelectQuery getUsersIdsSQL();

	SqlSelectQuery getGenresSQL();

	SqlIdsSelectQuery getGenresIdsSQL();

	SqlLogicallyJoinable equalsCondition( final String tableName, final String columnName, final long value );

	SqlLogicallyJoinable grateOrEqualsCondition( final String tableName, final String columnName, final long value );

	SqlLogicallyJoinable grateThenCondition( final String tableName, final String columnName, final long value );

	SqlLogicallyJoinable lessOrEqualsCondition( final String tableName, final String columnName, final long value );

	SqlLogicallyJoinable lessCondition( final String tableName, final String columnName, final long value );

	SqlCondition getCondition( final String tableName, final String columnName, final long value, final SqlCriteriaOperator operator );

	void addDescSortByUploadTimeDesc( final BaseSqlSelectQuery selectQuery );

	void addSortBySumVotingMarksDesc( final BaseSqlSelectQuery selectQuery );

	void addSortBySumVotingTimeDesc( final BaseSqlSelectQuery selectQuery );

	void addUserSortByName( final BaseSqlSelectQuery selectQuery );
}
