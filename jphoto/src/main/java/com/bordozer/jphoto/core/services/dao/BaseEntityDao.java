package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.interfaces.BaseEntity;
import com.bordozer.jphoto.core.interfaces.IdsSqlSelectable;
import com.bordozer.jphoto.sql.SqlSelectResult;
import com.bordozer.jphoto.sql.builder.SqlSelectQuery;

public interface BaseEntityDao<T extends BaseEntity> extends IdsSqlSelectable {

    String ENTITY_ID = "id";

    boolean saveToDB(final T entry);

    T load(final int entryId);

    SqlSelectResult<T> load(final SqlSelectQuery selectQuery);

    boolean delete(final int entryId);

    boolean exists(final T entry);

    boolean exists(final int entryId);
}
