package core.exceptions;

public class AccessDeniedException extends BaseRuntimeException {

	public AccessDeniedException() {
		super( "AccessDeniedException" );
	}

	public AccessDeniedException( final String message ) {
		super( message );
	}

	public AccessDeniedException( final Throwable cause ) {
		super( cause );
	}
}
