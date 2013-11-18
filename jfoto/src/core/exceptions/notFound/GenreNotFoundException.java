package core.exceptions.notFound;

import utils.TranslatorUtils;

public class GenreNotFoundException extends AbstractEntryNotFoundException {

	public GenreNotFoundException( final String _entryId ) {
		super( _entryId, TranslatorUtils.translate( "Photo category" ) );
	}

	public GenreNotFoundException( final int genreId ) {
		this( String.valueOf( genreId ) );
	}
}
