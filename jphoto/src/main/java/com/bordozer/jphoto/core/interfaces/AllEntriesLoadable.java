package com.bordozer.jphoto.core.interfaces;

import com.bordozer.jphoto.core.general.base.AbstractBaseEntity;

import java.util.List;

public interface AllEntriesLoadable<T extends AbstractBaseEntity> {

    List<T> loadAll();
}
