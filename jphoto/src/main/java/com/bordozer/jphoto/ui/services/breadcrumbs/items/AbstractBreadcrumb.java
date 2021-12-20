package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;

public abstract class AbstractBreadcrumb {

    protected final Services services;

    public abstract String getValue(final Language language);

    public AbstractBreadcrumb(final Services services) {
        this.services = services;
    }

    protected EntityLinkUtilsService getEntityLinkUtilsService() {
        return services.getEntityLinkUtilsService();
    }

    protected TranslatorService getTranslatorService() {
        return services.getTranslatorService();
    }

    protected DateUtilsService getDateUtilsService() {
        return services.getDateUtilsService();
    }
}
