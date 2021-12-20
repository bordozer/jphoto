package com.bordozer.jphoto.admin.controllers.translator;

public enum TranslationMode {
    TRANSLATED("translated", "Translator: Translated"), UNTRANSLATED("untranslated", "Translator: Untranslated"), UNUSED_TRANSLATIONS("unused", "Translator: unused translations");

    private final String prefix;
    private final String name;

    TranslationMode(final String prefix, final String name) {
        this.prefix = prefix;
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }
}
