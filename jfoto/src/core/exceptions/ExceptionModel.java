package core.exceptions;

import core.exceptions.notFound.NotFoundExceptionEntryType;

public class ExceptionModel {

	private String exceptionUrl;
	private String refererUrl;
	private String exceptionMessage;
	private NotFoundExceptionEntryType notFoundExceptionEntryType;

	public String getExceptionUrl() {
		return exceptionUrl;
	}

	public void setExceptionUrl( final String exceptionUrl ) {
		this.exceptionUrl = exceptionUrl;
	}

	public String getRefererUrl() {
		return refererUrl;
	}

	public void setRefererUrl( final String refererUrl ) {
		this.refererUrl = refererUrl;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage( final String exceptionMessage ) {
		this.exceptionMessage = exceptionMessage;
	}

	public void setNotFoundExceptionEntryType( final NotFoundExceptionEntryType notFoundExceptionEntryType ) {
		this.notFoundExceptionEntryType = notFoundExceptionEntryType;
	}

	public NotFoundExceptionEntryType getNotFoundExceptionEntryType() {
		return notFoundExceptionEntryType;
	}
}
