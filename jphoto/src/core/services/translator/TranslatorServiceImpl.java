package core.services.translator;

import core.services.utils.SystemVarsService;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

public class TranslatorServiceImpl implements TranslatorService {

	public static final String TRANSLATIONS_XML = "translations.xml";

	private Translator translator;

	private final Map<String, TranslationData> untranslatedMap = newHashMap();

	@Autowired
	private SystemVarsService systemVarsService;

	@Override
	public String translate( final String nerd ) {
		return translateWithParameters( nerd );
	}

	@Override
	public String translate( final String nerd, final String param ) {
		return translateWithParameters( nerd, param );
	}

	@Override
	public String translate( final String nerd, final String param1, final String param2 ) {
		return translateWithParameters( nerd, param1, param2 );
	}

	@Override
	public String translate( final String nerd, final String param1, final String param2, final String param3 ) {
		return translateWithParameters( nerd, param1, param2, param3 );
	}

	@Override
	public String translate( final String nerd, final String param1, final String param2, final String param3, final String param4 ) {
		return translateWithParameters( nerd, param1, param2, param3, param4 );
	}

	@Override
	public String translate( final String nerd, final int param1, final int param2 ) {
		return translateWithParameters( nerd, String.valueOf( param1 ), String.valueOf( param2 ) );
	}

	@Override
	public String translate( final String nerd, final long param ) {
		return translateWithParameters( nerd, String.valueOf( param ) );
	}

	@Override
	public String translateWithParameters( final String nerd, final String... params ) {

		final TranslationEntry translation = translator.getTranslation( nerd, Language.RU );

		if ( translation instanceof TranslationEntryNerd ) {
			addUntranslated( nerd, translation );
		}

		String result = translation.getValue(); // TODO: set System or User defined Language

		int i = 1;
		for ( String param : params ) {
			result = result.replace( String.format( "$%d", i++ ), param );
		}

		return markAsTranslated( result );
	}

	private void addUntranslated( final String nerd, final TranslationEntry translation ) {
		synchronized ( untranslatedMap ) {
			final TranslationData translationData = untranslatedMap.get( nerd );
			if ( translationData == null ) {
				final TranslationData translations = new TranslationData( nerd, newHashSet( translation ) );
				untranslatedMap.put( nerd, translations );
			} else {
				if ( ! hasTranslation( translationData, translation.getLanguage() ) ) {
					final Set<TranslationEntry> translations = translationData.getTranslations();
					translations.add( new TranslationEntry( nerd, translation.getLanguage(), translation.getValue() ) );
					untranslatedMap.put( nerd, new TranslationData( nerd, translations ) );
				}
			}
		}
	}

	private boolean hasTranslation( final TranslationData translationData, final Language language ) {
		final Set<TranslationEntry> translations = translationData.getTranslations();

		for ( final TranslationEntry _translation : translations ) {
			if ( _translation.getLanguage() == language ) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Map<String, TranslationData> getTranslationsMap() {
		return translator.getTranslationsMap();
	}

	@Override
	public Map<String, TranslationData> getUntranslatedMap() {
		return untranslatedMap;
	}

	public void initTranslations() throws DocumentException {

		final File translationsFile = new File( systemVarsService.getPropertiesPath(), TRANSLATIONS_XML );

		translator = TranslationsReader.getTranslator( translationsFile );
	}

	private String markAsTranslated( final String nerd ) {
		final String translatedSign = systemVarsService.getTranslatedSign();
		return String.format( "%s%s", nerd, StringUtils.isNotEmpty( translatedSign ) ? translatedSign : StringUtils.EMPTY );
	}

	public void setSystemVarsService( final SystemVarsService systemVarsService ) {
		this.systemVarsService = systemVarsService;
	}
}
