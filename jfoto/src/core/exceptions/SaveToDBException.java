package core.exceptions;

public class SaveToDBException extends Exception {

	public SaveToDBException( final String message ) {
		super( message );
	}

	public SaveToDBException( final Throwable cause ) {
		super( cause );
	}
}
