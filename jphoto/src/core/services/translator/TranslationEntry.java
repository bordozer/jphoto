package core.services.translator;

public class TranslationEntry {

	private final Language language;
	private final String value;

	protected final String nerd;

	public TranslationEntry( final String nerd, final Language language, final String value ) {
		this.nerd = nerd;
		this.language = language;
		this.value = value;
	}

	public Language getLanguage() {
		return language;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format( "%s: %s '%s'", nerd, language, value );
	}
}
