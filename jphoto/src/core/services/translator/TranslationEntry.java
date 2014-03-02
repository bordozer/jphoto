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

	public String getNerd() {
		return nerd;
	}

	public Language getLanguage() {
		return language;
	}

	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return nerd.hashCode() * 31;
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof TranslationEntry ) ) {
			return false;
		}

		final TranslationEntry translationEntry = ( TranslationEntry ) obj;
		return translationEntry.getLanguage() == language && translationEntry.getNerd().equals( nerd );
	}

	@Override
	public String toString() {
		return String.format( "%s: %s '%s'", nerd, language, value );
	}
}
