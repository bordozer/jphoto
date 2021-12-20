package com.bordozer.jphoto.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class ErrorUtils {

    public static StringBuilder getErrorStack(final Throwable throwable) {
        final String[] traceElements = ExceptionUtils.getStackFrames(throwable);
        final StringBuilder error = new StringBuilder();

        for (String traceElement : traceElements) {
            error.append(String.format("%s\n", traceElement));
        }
        return error;
    }
}
