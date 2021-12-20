package com.bordozer.jphoto.core.services.system;

import com.bordozer.jphoto.core.general.cache.CacheEntryFactory;
import com.bordozer.jphoto.core.general.cache.CacheKey;
import com.bordozer.jphoto.core.general.cache.keys.CacheCompositeKey;
import com.bordozer.jphoto.core.interfaces.Cacheable;

public interface CacheService<T extends Cacheable> {

    T getEntry(final CacheKey key, final CacheCompositeKey compositeKey, final CacheEntryFactory<T> entryFactory);

    void expire(final CacheKey key, CacheCompositeKey compositeKey);

    T getEntry(final CacheKey key, final int id, final CacheEntryFactory<T> entryFactory);

    void expire(final CacheKey key, final int id);

    void expire(final CacheKey key);
}
