package core.exceptions.notFound;

import utils.TranslatorUtils;

public enum NotFoundExceptionEntryType {
	USER( "Member" )
	, PHOTO( "Photo" )
	, GENRE( "Photo category" );

	private final String name;

	private NotFoundExceptionEntryType( final String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getNameTranslated() {
		return TranslatorUtils.translate( name );
	}
}
