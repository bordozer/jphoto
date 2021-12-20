package com.bordozer.jphoto.admin.controllers.translator.custom;

import com.bordozer.jphoto.core.services.translator.Language;

public class GenreTranslationEntry {

    private Language language;
    private int entryId;
    private String translation;

    public GenreTranslationEntry() {
    }

    public GenreTranslationEntry(final int entryId, final Language language, final String translation) {
        this.language = language;
        this.translation = translation;
        this.entryId = entryId;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(final int entryId) {
        this.entryId = entryId;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(final Language language) {
        this.language = language;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(final String translation) {
        this.translation = translation;
    }
}
