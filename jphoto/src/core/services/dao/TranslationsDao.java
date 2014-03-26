package core.services.dao;

import admin.controllers.translator.translations.TranslationEntryType;
import core.services.translator.Language;

public interface TranslationsDao {

	String translateCustom( final TranslationEntryType entryType, final int entryId, final Language language );

	boolean save( final TranslationEntryType entryType, final int entryId, final Language language, final String translation );
}
