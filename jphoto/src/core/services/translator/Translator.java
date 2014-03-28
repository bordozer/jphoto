package core.services.translator;

import core.services.utils.SystemVarsService;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class Translator {

	private final Map<NerdKey, TranslationData> translationsMap;
	private final Map<NerdKey, TranslationData> untranslatedMap = newHashMap();

	private SystemVarsService systemVarsService;

	public Translator( final Map<NerdKey, TranslationData> translationsMap, final SystemVarsService systemVarsService ) {
		this.translationsMap = translationsMap;
		this.systemVarsService = systemVarsService;
	}

	public TranslationEntry getTranslation( final String nerd, final Language language ) {

		final NerdKey key = new NerdKey( nerd );

		if ( ! translationsMap.containsKey( key ) ) {
			return new TranslationEntryMissed( nerd, language, systemVarsService );
		}

		return translationsMap.get( key ).getTranslationEntry( language );
	}

	public String translate( final String nerd, final Language language ) {
		return getTranslation( nerd, language ).getValueWithPrefixes();
	}

	public void registerTranslationEntry( final TranslationEntry translationEntry ) {
		final NerdKey nerdKey = new NerdKey( translationEntry.getNerd() );
		if ( ! translationsMap.containsKey( nerdKey ) ) {
			synchronized ( translationsMap ) {
				if ( ! translationsMap.containsKey( nerdKey ) ) {
					addToMap( translationsMap, translationEntry );
				}
			}
		}
	}

	public void registerNotTranslationEntry( final TranslationEntry translationEntry ) {

		registerTranslationEntry( translationEntry );

		final NerdKey nerdKey = new NerdKey( translationEntry.getNerd() );
		if ( ! untranslatedMap.containsKey( nerdKey ) ) {
			synchronized ( untranslatedMap ) {
				if ( ! untranslatedMap.containsKey( nerdKey ) ) {
					addToMap( untranslatedMap, translationEntry );
				}
			}
		}
	}

	private void addToMap( final Map<NerdKey, TranslationData> map, final TranslationEntry translationEntry ) {
		final String nerd = translationEntry.getNerd();
		final NerdKey nerdKey = new NerdKey( nerd );
		final TranslationData translationData = map.get( nerdKey );

		if ( translationData == null ) {
			map.put( nerdKey, new TranslationData( nerd, newArrayList( translationEntry ) ) );
		} else {
			if ( ! hasTranslationEntryForLanguage( translationData, translationEntry.getLanguage() ) ) {
				final List<TranslationEntry> translations = translationData.getTranslations();
				translations.add( new TranslationEntryMissed( nerd, translationEntry.getLanguage(), systemVarsService ) );
				map.put( nerdKey, new TranslationData( nerd, translations ) );
			}
		}
	}

	private boolean hasTranslationEntryForLanguage( final TranslationData translationData, final Language language ) {
		final List<TranslationEntry> translationEntries = translationData.getTranslations();

		for ( final TranslationEntry translationEntry : translationEntries ) {
			if ( translationEntry.getLanguage() == language ) {
				return true;
			}
		}

		return false;
	}

	public Map<NerdKey, TranslationData> getTranslationsMap() {
		return translationsMap;
	}

	public Map<NerdKey, TranslationData> getUntranslatedMap() {
		return untranslatedMap;
	}

	public void clearUntranslatedMap() {
		untranslatedMap.clear();
	}
}
