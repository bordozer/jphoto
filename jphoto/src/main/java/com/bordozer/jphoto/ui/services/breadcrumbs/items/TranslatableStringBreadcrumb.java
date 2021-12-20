package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class TranslatableStringBreadcrumb extends AbstractBreadcrumb {

    private String breadcrumb;

    public TranslatableStringBreadcrumb(final String breadcrumb, final Services services) {
        super(services);
        this.breadcrumb = breadcrumb;
    }

    @Override
    public String getValue(final Language language) {
        return getTranslatorService().translate(breadcrumb, language);
    }
}
