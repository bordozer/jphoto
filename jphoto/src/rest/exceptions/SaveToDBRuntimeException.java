package rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
public class SaveToDBRuntimeException extends RuntimeException {

	public SaveToDBRuntimeException( final String message ) {
		super( message );
	}

	public SaveToDBRuntimeException( final Throwable cause ) {
		super( cause );
	}
}
