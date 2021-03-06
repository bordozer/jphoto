package com.bordozer.jphoto.core.exceptions;

import com.bordozer.jphoto.core.exceptions.notFound.NotFoundExceptionEntryType;
import com.bordozer.jphoto.core.services.translator.TranslatorService;

public class ExceptionModel {

    private String exceptionUrl;
    private String refererUrl;
    private String exceptionMessage;
    private NotFoundExceptionEntryType notFoundExceptionEntryType;

    private TranslatorService translatorService;
    private BaseRuntimeException exception;

    public String getExceptionUrl() {
        return exceptionUrl;
    }

    public void setExceptionUrl(final String exceptionUrl) {
        this.exceptionUrl = exceptionUrl;
    }

    public String getRefererUrl() {
        return refererUrl;
    }

    public void setRefererUrl(final String refererUrl) {
        this.refererUrl = refererUrl;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(final String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public void setNotFoundExceptionEntryType(final NotFoundExceptionEntryType notFoundExceptionEntryType) {
        this.notFoundExceptionEntryType = notFoundExceptionEntryType;
    }

    public NotFoundExceptionEntryType getNotFoundExceptionEntryType() {
        return notFoundExceptionEntryType;
    }

    public TranslatorService getTranslatorService() {
        return translatorService;
    }

    public void setTranslatorService(final TranslatorService translatorService) {
        this.translatorService = translatorService;
    }

    public void setException(final BaseRuntimeException exception) {
        this.exception = exception;
    }

    public BaseRuntimeException getException() {
        return exception;
    }
}
