package core.services.translator;

import java.util.Map;

public class Translator {

	private Map<String, TranslationData> translationsMap;

	public Translator( final Map<String, TranslationData> translationsMap ) {
		this.translationsMap = translationsMap;
	}

	public TranslationEntry getTranslation( final String nerd, final Language language ) {

		if ( ! translationsMap.containsKey( nerd ) ) {
			return new TranslationEntryNerd( nerd );
		}

		return translationsMap.get( nerd ).getTranslationEntry( language );
	}

	public String translate( final String nerd, final Language ru ) {
		return getTranslation( nerd, Language.RU ).getValue();
	}

	public Map<String, TranslationData> getTranslationsMap() {
		return translationsMap;
	}
}
