package core.services.translator;

public enum TranslationEntryType {

	TRANSLATED( "Translated" )
	, NERD_TRANSLATION( "Nerd" )
	, MISSED_LANGUAGE( "Language tag is missed in translation.xml" )
	;

	private final String description;

	private TranslationEntryType( final String description ) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
