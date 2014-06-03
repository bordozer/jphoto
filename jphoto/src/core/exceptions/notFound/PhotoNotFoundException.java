package core.exceptions.notFound;

public class PhotoNotFoundException extends AbstractEntryNotFoundException {

	public PhotoNotFoundException( final String _entryId ) {
		super( _entryId, "PhotoNotFoundException: Photo" );
	}

	public PhotoNotFoundException( final int photoId ) {
		this( String.valueOf( photoId ) );
	}
}
