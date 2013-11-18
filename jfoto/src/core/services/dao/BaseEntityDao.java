package core.services.dao;

import core.interfaces.BaseEntity;
import core.interfaces.IdsSqlSelectable;
import sql.SqlSelectResult;
import sql.builder.SqlSelectQuery;

public interface BaseEntityDao<T extends BaseEntity> extends IdsSqlSelectable {

	String ENTITY_ID = "id";

	boolean saveToDB( final T entry );

	T load( final int entryId );

	SqlSelectResult<T> load( final SqlSelectQuery selectQuery );

	boolean delete( final int entryId );

	boolean exists( final T entry );

	boolean exists( final int entryId );
}
