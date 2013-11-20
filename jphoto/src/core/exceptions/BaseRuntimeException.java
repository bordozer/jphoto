package core.exceptions;

import core.log.LogHelper;

public class BaseRuntimeException extends RuntimeException {

	private final LogHelper log = new LogHelper( BaseRuntimeException.class );

	public BaseRuntimeException( final String message ) {
		super( message );

		log.error( message );
	}

	public BaseRuntimeException( final Throwable cause ) {
		super( cause );

		log.error( cause );
	}
}
