package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.admin.controllers.translator.custom.TranslationEntryType;
import com.bordozer.jphoto.core.services.translator.Language;

public interface TranslationsDao {

    String translateCustom(final TranslationEntryType entryType, final int entryId, final Language language);

    boolean save(final TranslationEntryType entryType, final int entryId, final Language language, final String translation);
}
