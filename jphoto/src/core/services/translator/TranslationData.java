package core.services.translator;

import java.util.List;

public class TranslationData {

	private final String nerd;

	private final List<TranslationEntry> translations;

	public TranslationData( final String nerd, final List<TranslationEntry> translations ) {
		this.nerd = nerd;
		this.translations = translations;
	}

	public TranslationEntry getTranslationEntry( final Language language ) {
		for ( final TranslationEntry translationEntry : translations ) {
			if ( translationEntry.getLanguage() == language ) {
				return translationEntry;
			}
		}

		throw new IllegalStateException( String.format( "Translation for '%s' ( %s ) not found!", nerd, language ) );
	}

	public String getNerd() {
		return nerd;
	}

	public List<TranslationEntry> getTranslations() {
		return translations;
	}
}
