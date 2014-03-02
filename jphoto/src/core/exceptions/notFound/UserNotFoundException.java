package core.exceptions.notFound;

public class UserNotFoundException extends AbstractEntryNotFoundException {

	public UserNotFoundException( final String _entryId ) {
		super( _entryId, "Member" ); // TODO: translate
	}

	public UserNotFoundException( final int userId ) {
		this( String.valueOf( userId ) );
	}
}
