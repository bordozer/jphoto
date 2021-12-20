package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

import java.sql.Time;

public class FormattedDateBreadcrumb extends AbstractBreadcrumb {

    private Time date;

    public FormattedDateBreadcrumb(final Time date, final Services services) {
        super(services);
        this.date = date;
    }

    @Override
    public String getValue(final Language language) {
        return getDateUtilsService().formatDate(date);
    }
}
