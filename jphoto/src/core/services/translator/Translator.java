package core.services.translator;

import java.util.Map;

public class Translator {

	private Map<String, TranslationData> translationsMap;
	private final String startPrefix;
	private final String endPrefix;

	public Translator( final Map<String, TranslationData> translationsMap, final String startPrefix, final String endPrefix ) {
		this.translationsMap = translationsMap;

		this.startPrefix = startPrefix;
		this.endPrefix = endPrefix;
	}

	public TranslationEntry getTranslation( final String nerd, final Language language ) {

		if ( ! translationsMap.containsKey( nerd ) ) {
			return new TranslationEntryNerd( nerd, startPrefix, endPrefix );
		}

		return translationsMap.get( nerd ).getTranslationEntry( language );
	}

	public String translate( final String nerd, final Language language ) {
		return getTranslation( nerd, language ).getValue();
	}

	public Map<String, TranslationData> getTranslationsMap() {
		return translationsMap;
	}
}
