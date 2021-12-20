package com.bordozer.jphoto.admin.jobs.entries.resources;

import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.utils.CommonUtils;
import lombok.SneakyThrows;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class FakePhotoCommentLoader {

    private static final String PHOTO_COMMENTS_XML = "jobs/photo-comments.xml";
    private static final String COMMENT_TAG = "comment";

    private static final List<String> comments = newArrayList();

    public static String getRandomFakeComment(final Services services) {
        return services.getRandomUtilsService().getRandomGenericListElement(getFakeComments());
    }

    private static List<String> getFakeComments() {

        if (comments.size() > 0) {
            return comments;
        }

        synchronized (comments) {

            if (comments.size() > 0) {
                return comments;
            }

            return loadFakeComments();
        }
    }

    @SneakyThrows
    private static List<String> loadFakeComments() {
        final String translationXmlContext = CommonUtils.readResource(PHOTO_COMMENTS_XML);
        final Document document = DocumentHelper.parseText(translationXmlContext);
        final Iterator photosIterator = document.getRootElement().elementIterator(COMMENT_TAG);

        while (photosIterator.hasNext()) {
            final Element commentElement = (Element) photosIterator.next();
            final String comment = commentElement.getText();

            comments.add(comment);
        }

        return comments;
    }
}
