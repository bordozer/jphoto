package core.services.system;

import core.general.cache.CacheEntryFactory;
import core.general.cache.CacheKey;
import core.general.cache.keys.CacheCompositeKey;
import core.interfaces.Cacheable;

public interface CacheService<T extends Cacheable> {

	T getEntry( final CacheKey key, final CacheCompositeKey compositeKey, final CacheEntryFactory<T> entryFactory );

	void expire( final CacheKey key, CacheCompositeKey compositeKey );

	T getEntry( final CacheKey key, final int id, final CacheEntryFactory<T> entryFactory );

	void expire( final CacheKey key, final int id );

	void expire( final CacheKey key );
}
