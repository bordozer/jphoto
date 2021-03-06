package com.bordozer.jphoto.admin.jobs.entries.resources.photos;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.LocalCategory;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.utils.RandomUtilsService;
import com.bordozer.jphoto.core.services.utils.RandomUtilsServiceImpl;
import com.bordozer.jphoto.utils.CommonUtils;
import lombok.SneakyThrows;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class FakePhotoNameGenerator {

    private static final String SUBJECTS_XML = "jobs/fake-photo-subjects.xml";
    private static final String PREPOSITIONS_XML = "jobs/fake-photo-prepositions.xml";
    private static final String OTHER_XML = "jobs/fake-photo-details.xml";

    private static final String ITEM_TAG = "item";
    private static final String KEY_TAG = "key";
    private static final String VALUE_TAG = "value";

    private static final List<PhotoNameXMLData> subjects = newArrayList();
    private static final List<PhotoNameXMLData> prepositions = newArrayList();
    private static final List<PhotoNameXMLData> details = newArrayList();

    private final static List<String> punctuationMarks = newArrayList("?", "!", "!!!", "...", "?!");

    public static void main(String[] args) throws UnsupportedEncodingException {
        final Genre genre = new Genre();
        genre.setName(LocalCategory.ANIMALS.getName());

        final String name = getFakePhotoName(genre, new RandomUtilsServiceImpl());

        final byte bytes[] = name.getBytes("UTF-8");
        final String value = new String(bytes, "UTF-8");

        System.out.println(value);
    }

    public static String getFakePhotoName(final Genre genre, final RandomUtilsService randomUtilsService) {

        final List<PhotoNameXMLData> subjects = getSubjects();
        final List<PhotoNameXMLData> prepositions = getPrepositions();
        final List<PhotoNameXMLData> details = getDetails();

        final StringBuilder result = new StringBuilder(getRandomSubject(genre, subjects, randomUtilsService));

        if (randomUtilsService.getRandomInt(0, 10) > 3) {
            result.append(" ");
            result.append(getRandomCombination(prepositions, details, randomUtilsService));
        }

        if (randomUtilsService.getRandomInt(0, 10) > 7) {
            result.append(" ");
            result.append(getRandomCombination(prepositions, details, randomUtilsService));
        }

        if (randomUtilsService.getRandomInt(0, 10) > 9) {
            result.append(randomUtilsService.getRandomGenericListElement(punctuationMarks));
        }

        return result.toString();
    }

    private static String getRandomSubject(final Genre genre, final List<PhotoNameXMLData> subjects, final RandomUtilsService randomUtilsService) {

        final List<PhotoNameXMLData> sbj = newArrayList(subjects);
        CollectionUtils.filter(sbj, new Predicate<PhotoNameXMLData>() {
            @Override
            public boolean evaluate(final PhotoNameXMLData photoNameXMLData) {
                return photoNameXMLData.getKey().equals(genre.getName());
            }
        });

        if (sbj.size() == 0) {
            for (final PhotoNameXMLData subject : subjects) {
                if (subject.getKey().equals(LocalCategory.OTHER.getName())) {
                    return randomUtilsService.getRandomGenericListElement(subject.getValues());
                }
            }
        }

        final List<String> subjectsForGenre = sbj.get(0).getValues();
        return randomUtilsService.getRandomGenericListElement(subjectsForGenre);
    }

    private static String getRandomCombination(final List<PhotoNameXMLData> prepositions, final List<PhotoNameXMLData> details, final RandomUtilsService randomUtilsService) {

        final PhotoNameXMLData randomCase = randomUtilsService.getRandomGenericListElement(prepositions);

        final String randomPreposition = randomUtilsService.getRandomGenericListElement(randomCase.getValues());

        for (final PhotoNameXMLData detail : details) {
            final boolean isCaseTheSame = detail.getKey().equals(randomCase.getKey());
            if (isCaseTheSame) {
                final String randomOther = randomUtilsService.getRandomGenericListElement(detail.getValues());
                return String.format("%s %s", randomPreposition, randomOther);
            }
        }

        throw new IllegalStateException(String.format("FakePhotoNameGenerator: can not generate random photo name"));
    }

    private static List<PhotoNameXMLData> getSubjects() {

        if (subjects.size() > 0) {
            return subjects;
        }

        synchronized (subjects) {

            if (subjects.size() > 0) {
                return subjects;
            }

            return loadCaseSensitiveData(SUBJECTS_XML, subjects);
        }
    }

    private static List<PhotoNameXMLData> getPrepositions() {
        if (prepositions.size() > 0) {
            return prepositions;
        }

        synchronized (prepositions) {

            if (prepositions.size() > 0) {
                return prepositions;
            }

            return loadCaseSensitiveData(PREPOSITIONS_XML, prepositions);
        }
    }

    private static List<PhotoNameXMLData> getDetails() {
        if (details.size() > 0) {
            return details;
        }

        synchronized (details) {

            if (details.size() > 0) {
                return details;
            }

            return loadCaseSensitiveData(OTHER_XML, details);
        }
    }

    @SneakyThrows
    private static List<PhotoNameXMLData> loadCaseSensitiveData(final String fileName, final List<PhotoNameXMLData> list) {
        final String translationXmlContext = CommonUtils.readResource(fileName);
        final Document document = DocumentHelper.parseText(translationXmlContext);
        final Iterator itemsIterator = document.getRootElement().elementIterator(ITEM_TAG);

        while (itemsIterator.hasNext()) {
            final Element itemElement = (Element) itemsIterator.next();

            final String caseName = itemElement.element(KEY_TAG).getText();

            final List<String> values = newArrayList();
            final Iterator valuesIterator = itemElement.elementIterator(VALUE_TAG);
            while (valuesIterator.hasNext()) {
                final Element valueElement = (Element) valuesIterator.next();
                values.add(valueElement.getText());
            }

            final PhotoNameXMLData data = new PhotoNameXMLData(caseName, values);

            list.add(data);
        }

        return list;
    }
}
