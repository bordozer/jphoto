package com.bordozer.jphoto.core.exceptions;

import com.bordozer.jphoto.core.log.LogHelper;

public class BaseRuntimeException extends RuntimeException {

    private final LogHelper log = new LogHelper();

    public BaseRuntimeException(final String message) {
        super(message);

        log.error(message);
    }

    public BaseRuntimeException(final Throwable cause) {
        super(cause);

        log.error(cause);
    }
}
