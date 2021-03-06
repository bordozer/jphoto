package com.bordozer.jphoto.ui.activity;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoPreview;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.message.EmptyTranslatableMessage;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import com.bordozer.jphoto.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Date;

public class ActivityPhotoPreview extends AbstractPhotoActivityStreamEntry {

    protected static final String ACTIVITY_XML_TAG_PHOTO_PREVIEW_ID = "photoPreviewId";

    private PhotoPreview preview;

    public ActivityPhotoPreview(final User user, final Photo photo, final Date activityTime, final String activityXML, final Services services) throws DocumentException {
        super(user, photo, activityTime, ActivityType.PHOTO_PREVIEW, services);

        final Document document = DocumentHelper.parseText(activityXML);
        final Element rootElement = document.getRootElement();

        final int previewId = NumberUtils.convertToInt(rootElement.element(ACTIVITY_XML_TAG_PHOTO_PREVIEW_ID).getText());
        preview = services.getPhotoPreviewService().load(previewId);
    }

    public ActivityPhotoPreview(final PhotoPreview preview, final Services services) {
        super(preview.getUser(), preview.getPhoto(), preview.getPreviewTime(), ActivityType.PHOTO_PREVIEW, services);

        this.preview = preview;
    }

    @Override
    public Document getActivityXML() {
        final Document document = super.getActivityXML();
        document.getRootElement().addElement(ACTIVITY_XML_TAG_PHOTO_PREVIEW_ID).addText(String.valueOf(preview.getId()));
        return document;
    }

    @Override
    protected TranslatableMessage getActivityTranslatableText() {
        if (preview == null) {
            return new EmptyTranslatableMessage();
        }

        return new TranslatableMessage("activity stream entry: viewed photo $1", services).addPhotoCardLinkParameter(preview.getPhoto());
    }

    @Override
    public String getDisplayActivityIcon() {
        if (preview == null) {
            return StringUtils.EMPTY;
        }

        return getPhotoIcon(preview.getPhoto());
    }

    @Override
    public String toString() {
        return String.format("%s: %s", getActivityType(), preview);
    }
}
