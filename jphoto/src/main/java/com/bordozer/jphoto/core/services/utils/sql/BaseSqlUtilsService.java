package com.bordozer.jphoto.core.services.utils.sql;

import com.bordozer.jphoto.sql.builder.BaseSqlSelectQuery;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;

public interface BaseSqlUtilsService {

    void initLimitAndOffset(final BaseSqlSelectQuery sqlSelectQuery, final int page, final int itemsOnPage);

    SqlIdsSelectQuery getUsersIdsSQL();

    SqlIdsSelectQuery getGenresIdsSQL();

    void addUserSortByName(final BaseSqlSelectQuery selectQuery);
}
