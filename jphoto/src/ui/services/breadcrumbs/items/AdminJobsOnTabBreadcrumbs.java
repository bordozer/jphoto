package ui.services.breadcrumbs.items;

import admin.jobs.enums.JobListTab;
import core.services.system.Services;
import core.services.translator.Language;

public class AdminJobsOnTabBreadcrumbs extends AbstractBreadcrumb {

	private JobListTab jobListTab;

	public AdminJobsOnTabBreadcrumbs( final JobListTab jobListTab, final Services services ) {
		super( services );
		this.jobListTab = jobListTab;
	}

	@Override
	public String getValue( final Language language ) {
		return getTranslatorService().translate( jobListTab.getName(), language );
	}
}
