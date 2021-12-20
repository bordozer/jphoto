package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class PortalPageBreadcrumb extends AbstractBreadcrumb {

    public PortalPageBreadcrumb(final Services services) {
        super(services);
    }

    @Override
    public String getValue(final Language language) {
        return getTranslatorService().translate(BreadcrumbsBuilder.BREADCRUMBS_PORTAL_PAGE, language);
    }
}
