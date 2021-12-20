package com.bordozer.jphoto.core.services.translator;

import com.bordozer.jphoto.admin.controllers.translator.custom.TranslationEntryType;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.dao.TranslationsDao;
import com.bordozer.jphoto.ui.dtos.TranslationDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.collect.Maps.newHashMap;

@Slf4j
@Service("translatorService")
public class TranslatorServiceImpl implements TranslatorService {

    private Translator translator;

    @Autowired
    private TranslationsDao translationsDao;

    @Override
    public String translate(final String nerd, final Language language, final String... params) {

        if (nerd.trim().length() == 0) {
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
        for (String param : params) {
            result = result.replace(String.format("$%d", i++), param);
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
    public Map<NerdKey, TranslationData> getUnusedTranslationsMap() {
        return translator.getUnusedTranslationsMap();
    }

    @PostConstruct
    @Override
    public void initTranslations() {
        this.translator = new Translator(newHashMap());
        final List<String> xmlContexts = getTranslationResourceContexts();
        xmlContexts.forEach(xmlContext -> translator.addTranslationMap(getTranslationMap(xmlContext)));
    }

    private static List<String> getTranslationResourceContexts() {
        final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            final Resource[] messageResources = resolver.getResources("classpath*:/translations/**/*.xml");
            return Arrays.stream(messageResources)
                    .map(getResourceContext())
                    .collect(Collectors.toList());
        } catch (final IOException e) {
            log.error("Resources list error", e);
            throw new IllegalStateException("Cannot get translation resources list", e);
        }
    }

    private Map<NerdKey, TranslationData> getTranslationMap(final String context) {
        return newHashMap(TranslationsReader.getTranslationMap(context));
    }

    private static Function<Resource, String> getResourceContext() {
        return LocalizationManager::resourceAsString;
    }

    @Override
    public void reloadTranslations() throws DocumentException {
        translator.clearUntranslatedMap();
        initTranslations();
    }

    @Override
    public TranslationDTO getTranslationAjax(final String nerd) {

        final Map<String, String> translations = newHashMap();

        for (final Language language : Language.values()) {
            final TranslationEntry translation = translator.getTranslation(nerd, language);
            translations.put(language.getCode(), translation.getValue());
        }

        return new TranslationDTO(translations);
    }

    @Override
    public String translateCustom(final TranslationEntryType entryType, final int entryId, final Language language) {
        return translationsDao.translateCustom(entryType, entryId, language);
    }

    @Override
    public String translateGenre(final int genreId, final Language language) {
        return translateCustom(TranslationEntryType.GENRE, genreId, language);
    }

    @Override
    public String translateGenre(final Genre genre, final Language language) {
        return translateGenre(genre.getId(), language);
    }

    @Override
    public String translatePhotoVotingCategory(final PhotoVotingCategory photoVotingCategory, final Language language) {
        return translateCustom(TranslationEntryType.VOTING_CATEGORY, photoVotingCategory.getId(), language);
    }

    @Override
    public boolean save(final TranslationEntryType entryType, final int entryId, final Language language, final String translation) {
        return translationsDao.save(entryType, entryId, language, translation);
    }

    public void setTranslator(final Translator translator) {
        this.translator = translator;
    }

    public void setTranslationsDao(final TranslationsDao translationsDao) {
        this.translationsDao = translationsDao;
    }
}
