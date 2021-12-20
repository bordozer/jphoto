package com.bordozer.jphoto.core.general.cache;

import com.bordozer.jphoto.core.interfaces.Cacheable;

public interface CacheEntryFactory<T extends Cacheable> {

    T createEntry();
}
