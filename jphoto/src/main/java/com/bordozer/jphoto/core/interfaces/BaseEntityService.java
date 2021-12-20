package com.bordozer.jphoto.core.interfaces;

import com.bordozer.jphoto.core.general.base.AbstractBaseEntity;

public interface BaseEntityService<T extends AbstractBaseEntity> {

    // Transactional
    boolean save(final T entry);

    T load(final int id);

    // Transactional
    boolean delete(final int entryId);

    boolean exists(final int entryId);

    boolean exists(final T entry);
}
