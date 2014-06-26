package admin.controllers.translator;

public enum TranslationMode {
	TRANSLATED( "translated" )
	, UNTRANSLATED( "untranslated" )
	;

	private final String prefix;

	TranslationMode( final String prefix ) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
}
