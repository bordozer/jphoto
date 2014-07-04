package ui.services.breadcrumbs.items;

import admin.jobs.enums.JobListTab;
import core.services.system.Services;
import core.services.translator.Language;

public class AdminJobsOnTabLinkBreadcrumbs extends AbstractBreadcrumb {

	private JobListTab jobListTab;

	public AdminJobsOnTabLinkBreadcrumbs( final JobListTab jobListTab, final Services services ) {
		super( services );
		this.jobListTab = jobListTab;
	}

	@Override
	public String getValue( final Language language ) {
		return getEntityLinkUtilsService().getAdminJobsOnTabLink( jobListTab, language );
	}
}
