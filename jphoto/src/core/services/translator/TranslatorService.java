package core.services.translator;

import admin.controllers.translator.translations.TranslationEntryType;
import core.dtos.TranslationDTO;
import org.dom4j.DocumentException;

import java.util.Map;

public interface TranslatorService {

	String BEAN_NAME = "translatorService";

	String translate( final String nerd );

	String translate( final String nerd, final String param );

	String translate( final String nerd, final String param1, final String param2 );

	String translate( final String nerd, final String param1, final String param2, final String param3 );

	String translate( final String nerd, final String param1, final String param2, final String param3, final String param4 );

	String translate( final String nerd, final int param1, final int param2 );

	String translate( final String nerd, final long param );

	String translateWithParameters( final String nerd, final String... params );

	Map<NerdKey, TranslationData> getTranslationsMap();

	Map<NerdKey, TranslationData> getUntranslatedMap();

	void initTranslations() throws DocumentException;

	void reloadTranslations() throws DocumentException;

	void reloadTranslationsAjax() throws DocumentException;

	TranslationDTO getTranslationAjax( final String nerd );

	void save( final TranslationEntryType entryType, final int entryId, final Language language, final String translation );
}
