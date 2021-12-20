package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class UserListLinkBreadcrumbs extends AbstractBreadcrumb {

    public UserListLinkBreadcrumbs(final Services services) {
        super(services);
    }

    @Override
    public String getValue(final Language language) {
        return getEntityLinkUtilsService().getUsersRootLink(language);
    }
}
