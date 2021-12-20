package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.admin.jobs.enums.JobListTab;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class AdminJobsOnTabLinkBreadcrumbs extends AbstractBreadcrumb {

    private JobListTab jobListTab;

    public AdminJobsOnTabLinkBreadcrumbs(final JobListTab jobListTab, final Services services) {
        super(services);
        this.jobListTab = jobListTab;
    }

    @Override
    public String getValue(final Language language) {
        return getEntityLinkUtilsService().getAdminJobsOnTabLink(jobListTab, language);
    }
}
