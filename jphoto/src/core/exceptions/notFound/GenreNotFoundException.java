package core.exceptions.notFound;

public class GenreNotFoundException extends AbstractEntryNotFoundException {

	public GenreNotFoundException( final String _entryId ) {
		super( _entryId, "Photo category" ); // TODO: translate
	}

	public GenreNotFoundException( final int genreId ) {
		this( String.valueOf( genreId ) );
	}
}
