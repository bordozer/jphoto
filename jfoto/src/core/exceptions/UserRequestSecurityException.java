package core.exceptions;

public class UserRequestSecurityException extends BaseRuntimeException {

	public UserRequestSecurityException( final String storedAuthorizationKey, final String requestAuthorizationKey ) {
		super( "Request Security Has Failed" );
	}
}
