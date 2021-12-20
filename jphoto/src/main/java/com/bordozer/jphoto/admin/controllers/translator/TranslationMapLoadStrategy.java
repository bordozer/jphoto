package com.bordozer.jphoto.admin.controllers.translator;

import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.NerdKey;
import com.bordozer.jphoto.core.services.translator.TranslationData;
import com.bordozer.jphoto.core.services.translator.TranslationEntry;
import com.bordozer.jphoto.core.services.translator.TranslationEntryMissed;
import com.bordozer.jphoto.core.services.translator.TranslationEntryNerd;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;

public abstract class TranslationMapLoadStrategy {

    private Map<NerdKey, TranslationData> translationMap;

    protected final TranslatorService translatorService;

    protected abstract Map<NerdKey, TranslationData> loadTranslationMap();

    public TranslationMapLoadStrategy(final TranslatorService translatorService) {
        this.translatorService = translatorService;
        translationMap = loadTranslationMapSorted();
    }

    public static TranslationMapLoadStrategy getInstance(final TranslatorService translatorService, final TranslationMode translationMode) {
        switch (translationMode) {
            case TRANSLATED:
                return getTranslatedMapLoadStrategy(translatorService);
            case UNTRANSLATED:
                return getUntranslatedMapLoadStrategy(translatorService);
        }

        throw new IllegalArgumentException(String.format("Illegal TranslationMode: #%s", translationMode));
    }

    public static TranslationMapLoadStrategy getTranslatedMapLoadStrategy(final TranslatorService translatorService) {

        return new TranslationMapLoadStrategy(translatorService) {

            @Override
            protected Map<NerdKey, TranslationData> loadTranslationMap() {

                final Map<NerdKey, TranslationData> translationMap1 = translatorService.getTranslationsMap();
                final HashMap<NerdKey, TranslationData> translatedDataOnlyMap = newHashMap();

                for (final NerdKey nerdKey : translationMap1.keySet()) {
                    final TranslationData translationData = translationMap1.get(nerdKey);
                    final List<TranslationEntry> translations = newArrayList(translationData.getTranslations());

                    CollectionUtils.filter(translations, new Predicate<TranslationEntry>() {
                        @Override
                        public boolean evaluate(final TranslationEntry translationEntry) {
                            return !(translationEntry instanceof TranslationEntryMissed) && !(translationEntry instanceof TranslationEntryNerd);
                        }
                    });

                    if (!translations.isEmpty()) {
                        translatedDataOnlyMap.put(nerdKey, new TranslationData(nerdKey.getNerd(), translations, translationData.getUsageIndex()));
                    }
                }

                return translatedDataOnlyMap;
            }
        };
    }

    public static TranslationMapLoadStrategy getUntranslatedMapLoadStrategy(final TranslatorService translatorService) {

        return new TranslationMapLoadStrategy(translatorService) {

            @Override
            protected Map<NerdKey, TranslationData> loadTranslationMap() {
                return translatorService.getUntranslatedMap();
            }
        };
    }

    public TranslationMapLoadStrategy filter(final String letter) {

        final HashMap<NerdKey, TranslationData> map = newLinkedHashMap(translationMap);

        for (final NerdKey nerdKey : translationMap.keySet()) {

            final TranslationData translationData = translationMap.get(nerdKey);

            final String nerd = translationData.getTranslationEntry(Language.NERD).getNerd();

            if (StringUtils.isEmpty(nerd)) {
                continue;
            }

            final String firstLetter = nerd.substring(0, 1);
            if (!firstLetter.equalsIgnoreCase(letter)) {
                map.remove(nerdKey);
            }
        }

        translationMap = map;

        return this;
    }

    public TranslationMapLoadStrategy filter(final Language language) {

        final HashMap<NerdKey, TranslationData> map = newLinkedHashMap();

        for (final NerdKey nerdKey : translationMap.keySet()) {

            final TranslationData translationData = translationMap.get(nerdKey);

            final List<TranslationEntry> translations = newArrayList(translationData.getTranslations());

            CollectionUtils.filter(translations, new Predicate<TranslationEntry>() {
                @Override
                public boolean evaluate(final TranslationEntry translationEntry) {
                    return translationEntry.getLanguage() == language;
                }
            });

            if (!translations.isEmpty()) {
                map.put(nerdKey, new TranslationData(nerdKey.getNerd(), translations, translationData.getUsageIndex()));
            }
        }

        translationMap = map;

        return this;
    }

    public TranslationMapLoadStrategy filterUnused() {

        translationMap = translatorService.getUnusedTranslationsMap();

        return this;
    }

    public List<String> getLetters() {

        final Map<NerdKey, TranslationData> translationsMap = loadTranslationMapSorted();

        final List<String> result = newArrayList();

        for (final NerdKey nerdKey : translationsMap.keySet()) {
            final TranslationData translationData = translationsMap.get(nerdKey);
            final TranslationEntry translationEntry = translationData.getTranslationEntry(Language.NERD);

            if (translationEntry == null || StringUtils.isEmpty(translationEntry.getNerd())) {
                continue;
            }

            final String letter = translationEntry.getNerd().substring(0, 1).toUpperCase();
            if (!result.contains(letter)) {
                result.add(letter);
            }
        }

        Collections.sort(result, new Comparator<String>() {
            @Override
            public int compare(final String o1, final String o2) {
                return o1.compareTo(o2);
            }
        });

        return result;
    }

    private Map<NerdKey, TranslationData> loadTranslationMapSorted() {

        final TreeMap<NerdKey, TranslationData> sortedMap = new TreeMap<NerdKey, TranslationData>(new Comparator<NerdKey>() {
            @Override
            public int compare(final NerdKey o1, final NerdKey o2) {
                return o1.getNerd().compareTo(o2.getNerd());
            }
        });

        sortedMap.putAll(loadTranslationMap());

        return sortedMap;
    }

    public Map<NerdKey, TranslationData> getTranslationMap() {
        return translationMap;
    }
}
