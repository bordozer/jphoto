package com.bordozer.jphoto.core.exceptions;

public class DBException extends BaseRuntimeException {

    public DBException(String message) {
        super(message);
    }

    public DBException(Throwable cause) {
        super(cause);
    }
}
