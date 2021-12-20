package com.bordozer.jphoto.core.general.cache.keys;

public class IntegerCacheKey implements CacheCompositeKey {

    private final Integer id;

    public IntegerCacheKey(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof IntegerCacheKey)) {
            return false;
        }

        final IntegerCacheKey key = (IntegerCacheKey) obj;
        return key.getId() == id;
    }

    @Override
    public String toString() {
        return String.format("IntCacheKey: %d", id);
    }
}
