package core.services.translator;

import core.services.utils.SystemVarsService;

import java.util.Map;

public class Translator {

	private Map<NerdKey, TranslationData> translationsMap;
	private SystemVarsService systemVarsService;

	public Translator( final Map<NerdKey, TranslationData> translationsMap, final SystemVarsService systemVarsService ) {
		this.translationsMap = translationsMap;
		this.systemVarsService = systemVarsService;
	}

	public TranslationEntry getTranslation( final String nerd, final Language language ) {

		final NerdKey key = new NerdKey( nerd );

		if ( ! translationsMap.containsKey( key ) ) {
			return new TranslationEntryNerd( nerd, systemVarsService );
		}

		return translationsMap.get( key ).getTranslationEntry( language );
	}

	public String translate( final String nerd, final Language language ) {
		return getTranslation( nerd, language ).getValueWithPrefixes();
	}

	public Map<NerdKey, TranslationData> getTranslationsMap() {
		return translationsMap;
	}
}
