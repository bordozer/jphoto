package ui.services.breadcrumbs.items;

import core.services.system.Services;
import core.services.translator.Language;

public class ProjectNameBreadcrumb extends AbstractBreadcrumb {

	public ProjectNameBreadcrumb( final Services services ) {
		super( services );
	}

	@Override
	public String getValue( final Language language ) {
		return services.getSystemVarsService().getProjectName();
	}
}
