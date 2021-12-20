package com.bordozer.jphoto.core.interfaces;

import com.bordozer.jphoto.sql.SqlSelectIdsResult;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;

public interface IdsSqlSelectable {

    SqlSelectIdsResult load(final SqlIdsSelectQuery selectIdsQuery);
}
