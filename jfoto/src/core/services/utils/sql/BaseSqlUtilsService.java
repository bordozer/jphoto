package core.services.utils.sql;

import core.general.base.PagingModel;
import sql.builder.*;

public interface BaseSqlUtilsService {

	void initLimitAndOffset( BaseSqlSelectQuery sqlSelectQuery, PagingModel pagingModel );

	SqlSelectQuery getSqlSelectQuery( String table );

	SqlIdsSelectQuery getPhotosIdsSQL();

	SqlSelectQuery getUsersSQL();

	SqlIdsSelectQuery getUsersIdsSQL();

	SqlSelectQuery getGenresSQL();

	SqlIdsSelectQuery getGenresIdsSQL();

	SqlLogicallyJoinable equalsCondition( String tableName, String columnName, long value );

	SqlLogicallyJoinable grateOrEqualsCondition( String tableName, String columnName, long value );

	SqlLogicallyJoinable grateThenCondition( String tableName, String columnName, long value );

	SqlLogicallyJoinable lessOrEqualsCondition( String tableName, String columnName, long value );

	SqlLogicallyJoinable lessCondition( String tableName, String columnName, long value );

	SqlCondition getCondition( String tableName, String columnName, long value, SqlCriteriaOperator operator );

	void addDescSortByUploadTimeDesc( BaseSqlSelectQuery selectQuery );

	void addSortBySumVotingMarksDesc( BaseSqlSelectQuery selectQuery );

	void addSortBySumVotingTimeDesc( BaseSqlSelectQuery selectQuery );

	void addUserSortByName( BaseSqlSelectQuery selectQuery );
}
