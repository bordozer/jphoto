package core.services.translator;

import admin.controllers.translator.custom.TranslationEntryType;
import core.dtos.TranslationDTO;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.log.LogHelper;
import core.services.dao.TranslationsDao;
import core.services.utils.SystemVarsService;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class TranslatorServiceImpl implements TranslatorService {

	public static final String TRANSLATIONS_PATH = "../translations";

	private Translator translator;

	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private TranslationsDao translationsDao;

	private final LogHelper log = new LogHelper( TranslatorServiceImpl.class );

	@Override
	public String translate( final String nerd, final Language language, final String... params ) {

		if ( nerd.trim().length() == 0 ) {
			return nerd;
		}

		final TranslationEntry translationEntry = translator.getTranslation( nerd, language );

		if ( translationEntry instanceof TranslationEntryMissed ) {
			translator.registerNotTranslationEntry( translationEntry );
		}

		String result = systemVarsService.isShowTranslationSigns() ? translationEntry.getValueWithPrefixes() : translationEntry.getValue();

		int i = 1;
		for ( String param : params ) {
			result = result.replace( String.format( "$%d", i++ ), param );
		}

		return result;
	}

	@Override
	public Map<NerdKey, TranslationData> getTranslationsMap() {
		return translator.getTranslationsMap();
	}

	@Override
	public Map<NerdKey, TranslationData> getUntranslatedMap() {
		return translator.getUntranslatedMap();
	}

	@Override
	public void initTranslations() throws DocumentException {

		final Map<NerdKey, TranslationData> translationsMap = newHashMap();
		translator = new Translator( translationsMap );

		translator.addTranslationMap( getTranslationMap( new File( TRANSLATIONS_PATH ) ) );
	}

	private Map<NerdKey, TranslationData> getTranslationMap( final File dir ) {

		final File[] farr = dir.listFiles();
		if ( farr == null ) {
			return newHashMap();
		}

		final List<File> files = Arrays.asList( farr );

		final Map<NerdKey, TranslationData> result = newHashMap();
		for ( final File file : files ) {
			if ( file.isDirectory() ) {
				result.putAll( getTranslationMap( file ) );
				continue;
			}

			try {
				result.putAll( TranslationsReader.getTranslationMap( file ) );
			} catch ( final DocumentException e ) {
				log.error( String.format( "Can not load translation from file '%s'", file.getAbsolutePath() ), e );
			}
		}

		return result;
	}

	@Override
	public void reloadTranslations() throws DocumentException {

		translator.clearUntranslatedMap();

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
