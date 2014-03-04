package core.services.translator;

import java.util.Map;

public class Translator {

	private Map<NerdKey, TranslationData> translationsMap;
	private final String startPrefix;
	private final String endPrefix;

	public Translator( final Map<NerdKey, TranslationData> translationsMap, final String startPrefix, final String endPrefix ) {
		this.translationsMap = translationsMap;

		this.startPrefix = startPrefix;
		this.endPrefix = endPrefix;
	}

	public TranslationEntry getTranslation( final String nerd, final Language language ) {

		final NerdKey key = new NerdKey( nerd );

		if ( ! translationsMap.containsKey( key ) ) {
			return new TranslationEntryNerd( nerd, startPrefix, endPrefix );
		}

		return translationsMap.get( key ).getTranslationEntry( language );
	}

	public String translate( final String nerd, final Language language ) {
		return getTranslation( nerd, language ).getValue();
	}

	public Map<NerdKey, TranslationData> getTranslationsMap() {
		return translationsMap;
	}
}
