package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class PhotoNameBreadcrumb extends AbstractBreadcrumb {

    private Photo photo;

    public PhotoNameBreadcrumb(final Photo photo, final Services services) {
        super(services);
        this.photo = photo;
    }

    @Override
    public String getValue(final Language language) {
        return photo.getNameEscaped();
    }
}
