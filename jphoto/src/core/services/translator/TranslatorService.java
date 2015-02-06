package core.services.translator;

import admin.controllers.translator.custom.TranslationEntryType;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import org.dom4j.DocumentException;
import ui.dtos.TranslationDTO;

import java.util.Map;

public interface TranslatorService {

	String BEAN_NAME = "translatorService";

	String translate( final String nerd, final Language language, final String... params );

	Map<NerdKey, TranslationData> getTranslationsMap();

	Map<NerdKey, TranslationData> getUntranslatedMap();

	Map<NerdKey, TranslationData> getUnusedTranslationsMap();

	void initTranslations() throws DocumentException;

	void reloadTranslations() throws DocumentException;

	TranslationDTO getTranslationAjax( final String nerd );

	boolean save( final TranslationEntryType entryType, final int entryId, final Language language, final String translation );

	String translateCustom( final TranslationEntryType entryType, final int entryId, final Language language );

	String translateGenre( final Genre genre, final Language language );

	String translateGenre( final int genreId, final Language language );

	String translatePhotoVotingCategory( final PhotoVotingCategory photoVotingCategory, final Language language );
}
