package core.interfaces;

import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;

public interface IdsSqlSelectable {

	SqlSelectIdsResult load( final SqlIdsSelectQuery selectIdsQuery );
}
