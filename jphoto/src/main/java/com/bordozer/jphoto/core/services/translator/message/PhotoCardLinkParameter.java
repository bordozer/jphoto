package com.bordozer.jphoto.core.services.translator.message;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class PhotoCardLinkParameter extends AbstractTranslatableMessageParameter {

    private Photo photo;

    protected PhotoCardLinkParameter(final Photo photo, final Services services) {
        super(services);
        this.photo = photo;
    }

    @Override
    public String getValue(final Language language) {
        return getEntityLinkUtilsService().getPhotoCardLink(photo, language);
    }
}
