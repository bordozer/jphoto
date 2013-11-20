package core.interfaces;

import core.general.base.AbstractBaseEntity;

import java.util.List;

public interface AllEntriesLoadable<T extends AbstractBaseEntity> {

	List<T> loadAll();
}
