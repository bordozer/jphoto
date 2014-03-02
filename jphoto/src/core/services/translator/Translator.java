package core.services.translator;

import java.util.Map;

public class Translator {

	private Map<String, TranslationData> translationsMap;

	public Translator( final Map<String, TranslationData> translationsMap ) {
		this.translationsMap = translationsMap;
	}

	public TranslationEntry getTranslation( final String nerd, final Language language ) {

		for ( final String _nerd : translationsMap.keySet() ) {
			if ( _nerd.equals( nerd ) ) {
				return translationsMap.get( _nerd ).getTranslationEntry( language );
			}
		}

		return new TranslationEntryNerd( nerd );
	}

	public String translate( final String nerd, final Language ru ) {
		return getTranslation( nerd, Language.RU ).getValue();
	}
}
