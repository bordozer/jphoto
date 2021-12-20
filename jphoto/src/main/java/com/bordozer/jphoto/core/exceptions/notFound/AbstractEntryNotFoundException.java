package com.bordozer.jphoto.core.exceptions.notFound;

import com.bordozer.jphoto.core.exceptions.BaseRuntimeException;

public abstract class AbstractEntryNotFoundException extends BaseRuntimeException {

    public AbstractEntryNotFoundException(final String _entryId, final String entityName) {
        super(String.format("%s with ID = '%s' not found", entityName, _entryId)); // TODO: translate
    }
}
