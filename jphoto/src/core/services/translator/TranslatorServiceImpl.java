package core.services.translator;

import admin.controllers.translator.custom.TranslationEntryType;
import core.dtos.TranslationDTO;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.services.dao.TranslationsDao;
import core.services.utils.SystemVarsService;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class TranslatorServiceImpl implements TranslatorService {

	public static final String TRANSLATIONS_XML = "translations.xml";

	private Translator translator;

	private final Map<NerdKey, TranslationData> untranslatedMap = newHashMap();

	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private TranslationsDao translationsDao;

	@Override
	public String translate( final String nerd, final Language language, final String... params ) {

		if ( nerd.trim().length() == 0 ) {
			return nerd;
		}

		TranslationEntry translationEntry = translator.getTranslation( nerd, language );

		if ( translationEntry == null ) {
			translationEntry = new TranslationEntryMissed( nerd, language, systemVarsService );
		}

		if ( translationEntry instanceof TranslationEntryMissed ) {
			addUntranslated( nerd, translationEntry );
		}

		String result = translationEntry.getValueWithPrefixes();

		int i = 1;
		for ( String param : params ) {
			result = result.replace( String.format( "$%d", i++ ), param );
		}

		return result;
	}

	private void addUntranslated( final String nerd, final TranslationEntry translation ) {
		synchronized ( untranslatedMap ) {
			final NerdKey nerdKey = new NerdKey( nerd );
			final TranslationData translationData = untranslatedMap.get( nerdKey );
			if ( translationData == null ) {
				final TranslationData translations = new TranslationData( nerd, newArrayList( translation ) );
				untranslatedMap.put( nerdKey, translations );
			} else {
				if ( ! hasTranslation( translationData, translation.getLanguage() ) ) {
					final List<TranslationEntry> translations = translationData.getTranslations();
					translations.add( new TranslationEntryMissed( nerd, translation.getLanguage(), systemVarsService ) );
					untranslatedMap.put( nerdKey, new TranslationData( nerd, translations ) );
				}
			}
		}
	}

	private boolean hasTranslation( final TranslationData translationData, final Language language ) {
		final List<TranslationEntry> translations = translationData.getTranslations();

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
	public String translateGenre( final int genreId, final Language language ) {
		return translateCustom( TranslationEntryType.GENRE, genreId, language );
	}

	@Override
	public String translateGenre( final Genre genre, final Language language ) {
		return translateGenre( genre.getId(), language );
	}

	@Override
	public String translatePhotoVotingCategory( final PhotoVotingCategory photoVotingCategory, final Language language ) {
		return translateCustom( TranslationEntryType.VOTING_CATEGORY, photoVotingCategory.getId(), language );
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
