package core.interfaces;

import core.general.base.AbstractBaseEntity;

import java.util.List;

public interface AllEntriesByIdLoadable<T extends AbstractBaseEntity> {

	List<T> loadAllForEntry( final int entryId );

//	T load( final int entryId );
}
