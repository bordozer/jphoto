package core.services.utils.sql;

import sql.builder.BaseSqlSelectQuery;
import sql.builder.SqlIdsSelectQuery;

public interface BaseSqlUtilsService {

	void initLimitAndOffset( final BaseSqlSelectQuery sqlSelectQuery, final int page, final int itemsOnPage );

	SqlIdsSelectQuery getUsersIdsSQL();

	SqlIdsSelectQuery getGenresIdsSQL();

	void addUserSortByName( final BaseSqlSelectQuery selectQuery );
}
