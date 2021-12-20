package com.bordozer.jphoto.core.services.translator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.Maps.newHashMap;

@Slf4j
public class LocalizationManager {

    private static final Language DEFAULT_LANGUAGE = Language.EN;

    private final Translator translator;

    public LocalizationManager() {
        this.translator = new Translator(newHashMap());
        final List<String> xmlContexts = getTranslationResourceContexts();
        xmlContexts.forEach(xmlContext -> translator.addTranslationMap(getTranslationMap(xmlContext)));
    }

    private static List<String> getTranslationResourceContexts() {
        final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            final Resource[] messageResources = resolver.getResources("classpath*:/localizations/*.xml");
            return Arrays.stream(messageResources)
                    .map(getResourceContext())
                    .collect(Collectors.toList());
        } catch (final IOException e) {
            log.error("Resources list error", e);
            throw new IllegalStateException("Cannot get translation resources list", e);
        }
    }

    private static Function<Resource, String> getResourceContext() {
        return LocalizationManager::resourceAsString;
    }

    public static String resourceAsString(final Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (final IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public String translate(final String nerd, final Language language, final String... params) {
        if (StringUtils.isBlank(nerd)) {
            return nerd;
        }

        if (language == Language.NERD) {
            return nerd;
        }

        final TranslationEntry translationEntry = translator.getTranslation(nerd, language);

        if (translationEntry instanceof TranslationEntryMissed) {
            translator.registerNotTranslationEntry(translationEntry);
        }

        String result = translationEntry.getValue();

        int i = 1;
        for (final String param : params) {
            result = result.replace(String.format("$%d", i++), param);
        }

        return result;
    }

    public Map<NerdKey, TranslationData> getTranslationsMap() {
        return translator.getTranslationsMap();
    }

    public Map<NerdKey, TranslationData> getUntranslatedMap() {
        return translator.getUntranslatedMap();
    }

    public Map<NerdKey, TranslationData> getUnusedTranslationsMap() {
        return translator.getUnusedTranslationsMap();
    }

    private Map<NerdKey, TranslationData> getTranslationMap(final String context) {
        final Map<NerdKey, TranslationData> result = newHashMap();
        result.putAll(TranslationsReader.getTranslationMap(context));
        return result;
    }

    public Language getLanguage(final String code) {

        final Language language = Language.getByCode(code);

        if (language != null) {
            return language;
        }

        return getDefaultLanguage();
    }

    public Language getDefaultLanguage() {
        return DEFAULT_LANGUAGE;
    }
}
