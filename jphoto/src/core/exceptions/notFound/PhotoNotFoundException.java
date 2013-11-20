package core.exceptions.notFound;

import utils.TranslatorUtils;

public class PhotoNotFoundException extends AbstractEntryNotFoundException {

	public PhotoNotFoundException( final String _entryId ) {
		super( _entryId, TranslatorUtils.translate( "Photo" ) );
	}

	public PhotoNotFoundException( final int photoId ) {
		this( String.valueOf( photoId ) );
	}
}
