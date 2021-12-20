package com.bordozer.jphoto.admin.controllers.translator.custom;

import com.bordozer.jphoto.core.general.base.AbstractGeneralModel;
import com.bordozer.jphoto.core.services.translator.Language;

import java.util.List;
import java.util.Map;

public class CustomTranslationsModel extends AbstractGeneralModel {

    private int selectedLanguageId;
    private int selectedLanguageIdOld;

    private Map<Integer, GenreTranslationEntry> selectedLanguageTranslationEntriesMap;
    private Map<IdLanguageKey, GenreTranslationEntry> allTranslationEntriesMap;

    private Map<Integer, String> customTranslatableEntriesMap;
    private List<Language> languages;

    private TranslationEntryType translationEntryType;
    private String redirectToPrefix;

    public int getSelectedLanguageId() {
        return selectedLanguageId;
    }

    public void setSelectedLanguageId(final int selectedLanguageId) {
        this.selectedLanguageId = selectedLanguageId;
    }

    public int getSelectedLanguageIdOld() {
        return selectedLanguageIdOld;
    }

    public void setSelectedLanguageIdOld(final int selectedLanguageIdOld) {
        this.selectedLanguageIdOld = selectedLanguageIdOld;
    }

    public Map<Integer, GenreTranslationEntry> getSelectedLanguageTranslationEntriesMap() {
        return selectedLanguageTranslationEntriesMap;
    }

    public void setSelectedLanguageTranslationEntriesMap(final Map<Integer, GenreTranslationEntry> selectedLanguageTranslationEntriesMap) {
        this.selectedLanguageTranslationEntriesMap = selectedLanguageTranslationEntriesMap;
    }

    public Map<IdLanguageKey, GenreTranslationEntry> getAllTranslationEntriesMap() {
        return allTranslationEntriesMap;
    }

    public void setAllTranslationEntriesMap(final Map<IdLanguageKey, GenreTranslationEntry> allTranslationEntriesMap) {
        this.allTranslationEntriesMap = allTranslationEntriesMap;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(final List<Language> languages) {
        this.languages = languages;
    }

    public Map<Integer, String> getCustomTranslatableEntriesMap() {
        return customTranslatableEntriesMap;
    }

    public void setCustomTranslatableEntriesMap(final Map<Integer, String> customTranslatableEntriesMap) {
        this.customTranslatableEntriesMap = customTranslatableEntriesMap;
    }

    public TranslationEntryType getTranslationEntryType() {
        return translationEntryType;
    }

    public void setTranslationEntryType(final TranslationEntryType translationEntryType) {
        this.translationEntryType = translationEntryType;
    }

    public String getRedirectToPrefix() {
        return redirectToPrefix;
    }

    public void setRedirectToPrefix(final String redirectToPrefix) {
        this.redirectToPrefix = redirectToPrefix;
    }
}
