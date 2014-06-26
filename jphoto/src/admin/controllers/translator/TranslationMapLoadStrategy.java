package admin.controllers.translator;

import core.services.translator.*;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.lang.StringUtils;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public abstract class TranslationMapLoadStrategy {

	private Map<NerdKey, TranslationData> translationMap;

	protected final TranslatorService translatorService;

	protected abstract Map<NerdKey, TranslationData> loadTranslationMap();

	public TranslationMapLoadStrategy( final TranslatorService translatorService ) {
		this.translatorService = translatorService;
		translationMap = loadTranslationMapSorted();
	}

	public static TranslationMapLoadStrategy getInstance( final TranslatorService translatorService, final TranslationMode translationMode ) {
		switch ( translationMode ) {
			case TRANSLATED:
				return getTranslatedMapLoadStrategy( translatorService );
			case UNTRANSLATED:
				return getUntranslatedMapLoadStrategy( translatorService );
		}

		throw new IllegalArgumentException( String.format( "Illegal TranslationMode: #%s", translationMode ) );
	}

	public static TranslationMapLoadStrategy getTranslatedMapLoadStrategy( final TranslatorService translatorService ) {

		return new TranslationMapLoadStrategy( translatorService ) {

			@Override
			protected Map<NerdKey, TranslationData> loadTranslationMap() {

				final Map<NerdKey, TranslationData> translationMap = translatorService.getTranslationsMap();
				final HashMap<NerdKey, TranslationData> translatedDataOnlyMap = newHashMap();

				for ( final NerdKey nerdKey : translationMap.keySet() ) {
					final TranslationData translationData = translationMap.get( nerdKey );
					final List<TranslationEntry> translations = newArrayList( translationData.getTranslations() );

					CollectionUtils.filter( translations, new Predicate<TranslationEntry>() {
						@Override
						public boolean evaluate( final TranslationEntry translationEntry ) {
							return ! ( translationEntry instanceof TranslationEntryMissed ) && ! ( translationEntry instanceof TranslationEntryNerd );
						}
					} );

					if ( ! translations.isEmpty() ) {
						translatedDataOnlyMap.put( nerdKey, new TranslationData( nerdKey.getNerd(), translations ) );
					}
				}

				return translatedDataOnlyMap;
			}
		};
	}

	public static TranslationMapLoadStrategy getUntranslatedMapLoadStrategy( final TranslatorService translatorService ) {

		return new TranslationMapLoadStrategy( translatorService ) {

			@Override
			protected Map<NerdKey, TranslationData> loadTranslationMap() {
				return translatorService.getUntranslatedMap();
			}
		};
	}

	public TranslationMapLoadStrategy filter( final String letter ) {

		final HashMap<NerdKey, TranslationData> map = newHashMap( translationMap );

		for ( final NerdKey nerdKey : translationMap.keySet() ) {

			final TranslationData translationData = translationMap.get( nerdKey );

			final String nerd = translationData.getTranslationEntry( Language.NERD ).getNerd();

			if ( StringUtils.isEmpty( nerd ) ) {
				continue;
			}

			final String firstLetter = nerd.substring( 0, 1 );
			if ( !firstLetter.equalsIgnoreCase( letter ) ) {
				map.remove( nerdKey );
			}
		}

		translationMap = map;

		return this;
	}

	public TranslationMapLoadStrategy filter( final Language language ) {

		final HashMap<NerdKey, TranslationData> map = newHashMap();

		for ( final NerdKey nerdKey : translationMap.keySet() ) {

			final TranslationData translationData = translationMap.get( nerdKey );

			final List<TranslationEntry> translations = newArrayList( translationData.getTranslations() );

			CollectionUtils.filter( translations, new Predicate<TranslationEntry>() {
				@Override
				public boolean evaluate( final TranslationEntry translationEntry ) {
					return translationEntry.getLanguage() == language;
				}
			} );

			if ( ! translations.isEmpty() ) {
				map.put( nerdKey, new TranslationData( nerdKey.getNerd(), translations ) );
			}
		}

		translationMap = map;

		return this;
	}

	public List<String> getLetters() {

		final Map<NerdKey, TranslationData> translationsMap = loadTranslationMapSorted();

		final List<String> result = newArrayList();

		for ( final NerdKey nerdKey : translationsMap.keySet() ) {
			final TranslationData translationData = translationsMap.get( nerdKey );
			final TranslationEntry translationEntry = translationData.getTranslationEntry( Language.NERD );

			if ( translationEntry == null || StringUtils.isEmpty( translationEntry.getNerd() ) ) {
				continue;
			}

			final String letter = translationEntry.getNerd().substring( 0, 1 ).toUpperCase();
			if ( !result.contains( letter ) ) {
				result.add( letter );
			}
		}

		Collections.sort( result, new Comparator<String>() {
			@Override
			public int compare( final String o1, final String o2 ) {
				return o1.compareTo( o2 );
			}
		} );

		return result;
	}

	public Map<NerdKey, TranslationData> getTranslationMap() {
		return translationMap;
	}

	protected Map<NerdKey, TranslationData> loadTranslationMapSorted() {

		final TreeMap<NerdKey, TranslationData> sortedMap = new TreeMap<NerdKey, TranslationData>( new Comparator<NerdKey>() {
			@Override
			public int compare( final NerdKey o1, final NerdKey o2 ) {
				return o1.getNerd().compareTo( o2.getNerd() );
			}
		} );

		sortedMap.putAll( loadTranslationMap() );

		return sortedMap;
	}
}
