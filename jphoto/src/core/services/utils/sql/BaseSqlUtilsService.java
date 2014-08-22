package core.services.utils.sql;

import core.general.base.PagingModel;
import sql.builder.*;

public interface BaseSqlUtilsService {

	void initLimitAndOffset( final BaseSqlSelectQuery sqlSelectQuery, final PagingModel pagingModel );

	void initLimitAndOffset( final BaseSqlSelectQuery sqlSelectQuery, final int page, final int itemsOnPage );

	SqlIdsSelectQuery getPhotosIdsSQL();

	SqlIdsSelectQuery getUsersIdsSQL();

	SqlIdsSelectQuery getGenresIdsSQL();

	SqlLogicallyJoinable equalsCondition( final String tableName, final String columnName, final long value );

	void addDescSortByUploadTimeDesc( final BaseSqlSelectQuery selectQuery );

	void addSortBySumVotingMarksDesc( final BaseSqlSelectQuery selectQuery );

	void addSortBySumVotingTimeDesc( final BaseSqlSelectQuery selectQuery );

	void addUserSortByName( final BaseSqlSelectQuery selectQuery );
}
