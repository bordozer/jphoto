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

		return null;
	}

	public String getNerd() {
		return nerd;
	}

	public List<TranslationEntry> getTranslations() {
		return translations;
	}
}
