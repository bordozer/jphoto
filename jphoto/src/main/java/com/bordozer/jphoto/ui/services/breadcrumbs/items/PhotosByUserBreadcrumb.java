package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class PhotosByUserBreadcrumb extends AbstractBreadcrumb {

    private User user;

    public PhotosByUserBreadcrumb(final User user, final Services services) {
        super(services);
        this.user = user;
    }

    @Override
    public String getValue(final Language language) {
        return getEntityLinkUtilsService().getPhotosByUserLink(user, language);
    }
}
