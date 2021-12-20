package com.bordozer.jphoto.core.services.translator;

import lombok.SneakyThrows;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class TranslationsReader {

    private static final String TRANSLATION = "translation";

    @SneakyThrows
    public static Map<NerdKey, TranslationData> getTranslationMap(final String translationXmlContext) {
        final Document document = DocumentHelper.parseText(translationXmlContext);
        final Iterator<?> photosIterator = document.getRootElement().elementIterator(TRANSLATION);
        final Map<NerdKey, TranslationData> translationsMap = newHashMap();

        while (photosIterator.hasNext()) {

            final Element nerdElement = (Element) photosIterator.next();
            final String nerd = nerdElement.element(Language.NERD.getCode()).getText();

            final List<TranslationEntry> translations = newArrayList();
            translations.add(new TranslationEntryNerd(nerd));

            for (final Language language : Language.values()) {

                if (language == Language.NERD) {
                    continue;
                }

                final Element element = nerdElement.element(language.getCode());
                if (element == null) {
                    translations.add(new TranslationEntryMissed(nerd, language));
                    continue;
                }

                final String translation = element.getText();
                translations.add(new TranslationEntry(nerd, language, translation));
            }

            translationsMap.put(new NerdKey(nerd), new TranslationData(nerd, translations));
        }

        return translationsMap;
    }
}
