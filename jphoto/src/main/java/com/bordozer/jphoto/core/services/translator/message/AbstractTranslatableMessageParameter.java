package com.bordozer.jphoto.core.services.translator.message;

import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;

public abstract class AbstractTranslatableMessageParameter {

    protected final Services services;

    protected AbstractTranslatableMessageParameter(final Services services) {
        this.services = services;
    }

    public abstract String getValue(final Language language);

    protected TranslatorService getTranslatorService() {
        return services.getTranslatorService();
    }

    protected EntityLinkUtilsService getEntityLinkUtilsService() {
        return services.getEntityLinkUtilsService();
    }

    protected DateUtilsService getDateUtilsService() {
        return services.getDateUtilsService();
    }
}
