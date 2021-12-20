package com.bordozer.jphoto.core.interfaces;

import com.bordozer.jphoto.core.general.base.AbstractBaseEntity;

import java.util.List;

public interface AllEntriesByIdLoadable<T extends AbstractBaseEntity> {

    List<T> loadAllForEntry(final int entryId);

    //	T load( final int entryId );
}
