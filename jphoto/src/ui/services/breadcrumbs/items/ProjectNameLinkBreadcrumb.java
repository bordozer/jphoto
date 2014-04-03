package ui.services.breadcrumbs.items;

import core.services.system.Services;
import core.services.translator.Language;

public class ProjectNameLinkBreadcrumb extends AbstractBreadcrumb {

	public ProjectNameLinkBreadcrumb( final Services services ) {
		super( services );
	}

	@Override
	public String getValue( final Language language ) {
		return getEntityLinkUtilsService().getProjectNameLink( language );
	}
}
