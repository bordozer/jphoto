package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class AdminSavedJobTypeBreadcrumbs extends AbstractBreadcrumb {

    private SavedJobType savedJobType;

    public AdminSavedJobTypeBreadcrumbs(final SavedJobType savedJobType, final Services services) {
        super(services);
        this.savedJobType = savedJobType;
    }

    @Override
    public String getValue(final Language language) {
        return getTranslatorService().translate(savedJobType.getName(), language);
    }
}
