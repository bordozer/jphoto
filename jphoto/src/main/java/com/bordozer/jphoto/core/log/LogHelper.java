package com.bordozer.jphoto.core.log;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogHelper {

    public void debug(final String message) {
        log.debug(message);
    }

    public void debug(final String message, final Throwable throwable) {
        log.debug(message, throwable);
    }

    public void info(final String message) {
        log.info(message);
    }

    public void warn(final String message) {
        log.warn(message);
    }

    public void warn(final String message, final Throwable throwable) {
        log.warn(message, throwable);
    }

    public void error(final String message) {
        log.error(message);
    }

    public void error(final Throwable throwable) {
        log.error("Exception", throwable);
    }

    public void error(final String message, final Throwable throwable) {
        log.error(message, throwable);
    }
}
