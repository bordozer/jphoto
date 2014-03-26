package core.services.translator;

import admin.controllers.translator.custom.TranslationEntryType;
import core.dtos.TranslationDTO;
import core.general.genre.Genre;
import core.general.user.User;
import core.services.dao.TranslationsDao;
import core.services.utils.SystemVarsService;
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

	private final Map<NerdKey, TranslationData> untranslatedMap = newHashMap();

	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private TranslationsDao translationsDao;

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

		if ( nerd.trim().length() == 0 ) {
			return nerd;
		}

		final TranslationEntry translation = translator.getTranslation( nerd, Language.RU );

		if ( translation instanceof TranslationEntryNerd ) {
			addUntranslated( nerd, translation );
		}

		String result = translation.getValueWithPrefixes(); // TODO: set System or User defined Language

		int i = 1;
		for ( String param : params ) {
			result = result.replace( String.format( "$%d", i++ ), param );
		}

//		return markAsTranslated( result );
		return result;
	}

	private void addUntranslated( final String nerd, final TranslationEntry translation ) {
		synchronized ( untranslatedMap ) {
			final NerdKey nerdKey = new NerdKey( nerd );
			final TranslationData translationData = untranslatedMap.get( nerdKey );
			if ( translationData == null ) {
				final TranslationData translations = new TranslationData( nerd, newHashSet( translation ) );
				untranslatedMap.put( nerdKey, translations );
			} else {
				if ( ! hasTranslation( translationData, translation.getLanguage() ) ) {
					final Set<TranslationEntry> translations = translationData.getTranslations();
					translations.add( new TranslationEntry( nerd, translation.getLanguage(), translation.getValueWithPrefixes(), systemVarsService ) );
					untranslatedMap.put( nerdKey, new TranslationData( nerd, translations ) );
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
	public Map<NerdKey, TranslationData> getTranslationsMap() {
		return translator.getTranslationsMap();
	}

	@Override
	public Map<NerdKey, TranslationData> getUntranslatedMap() {
		return untranslatedMap;
	}

	@Override
	public void initTranslations() throws DocumentException {

		final File translationsFile = new File( systemVarsService.getPropertiesPath(), TRANSLATIONS_XML );

		translator = TranslationsReader.getTranslator( translationsFile, systemVarsService );
	}

	@Override
	public void reloadTranslations() throws DocumentException {

		untranslatedMap.clear();

		initTranslations();
	}

	@Override
	public void reloadTranslationsAjax() throws DocumentException {
		reloadTranslations();
	}

	@Override
	public TranslationDTO getTranslationAjax( final String nerd ) {

		final Map<String, String> translations = newHashMap();

		for ( final Language language : Language.values() ) {
			final TranslationEntry translation = translator.getTranslation( nerd, language );
			translations.put( language.getCode(), translation.getValue() );
		}

		return new TranslationDTO( translations );
	}

	@Override
	public String translateCustom( final TranslationEntryType entryType, final int entryId, final Language language ) {
		return translationsDao.translateCustom( entryType, entryId, language );
	}

	@Override
	public String translateGenre( final int entryId, final Language language ) {
		return translateCustom( TranslationEntryType.GENRE, entryId, language );
	}

	@Override
	public String translateGenre( final Genre genre, final Language language ) {
		return translateGenre( genre.getId(), language );
	}

	@Override
	public boolean save( final TranslationEntryType entryType, final int entryId, final Language language, final String translation ) {
		return translationsDao.save( entryType, entryId, language, translation );
	}

	public void setTranslator( final Translator translator ) {
		this.translator = translator;
	}

	public void setSystemVarsService( final SystemVarsService systemVarsService ) {
		this.systemVarsService = systemVarsService;
	}

	public void setTranslationsDao( final TranslationsDao translationsDao ) {
		this.translationsDao = translationsDao;
	}
}
