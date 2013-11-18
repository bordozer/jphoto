package core.exceptions.notFound;

import utils.TranslatorUtils;

public class UserNotFoundException extends AbstractEntryNotFoundException {

	public UserNotFoundException( final String _entryId ) {
		super( _entryId, TranslatorUtils.translate( "Member" ) );
	}

	public UserNotFoundException( final int userId ) {
		this( String.valueOf( userId ) );
	}
}
