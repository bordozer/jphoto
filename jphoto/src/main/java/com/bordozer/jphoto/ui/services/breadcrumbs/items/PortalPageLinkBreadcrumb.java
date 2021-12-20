package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class PortalPageLinkBreadcrumb extends AbstractBreadcrumb {

    public PortalPageLinkBreadcrumb(final Services services) {
        super(services);
    }

    @Override
    public String getValue(final Language language) {
        return getEntityLinkUtilsService().getPortalPageLink(language);
    }
}
