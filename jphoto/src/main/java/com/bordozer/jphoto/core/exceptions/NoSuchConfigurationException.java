package com.bordozer.jphoto.core.exceptions;

public class NoSuchConfigurationException extends BaseRuntimeException {

    public NoSuchConfigurationException(final String message) {
        super(message);
    }

    public NoSuchConfigurationException(final Throwable cause) {
        super(cause);
    }
}
