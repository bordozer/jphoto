package core.general.cache;

import core.interfaces.Cacheable;

public interface CacheEntryFactory<T extends Cacheable> {

	T createEntry();
}
