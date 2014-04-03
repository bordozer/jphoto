package ui.services.breadcrumbs.items;

import core.services.system.Services;
import core.services.translator.Language;

public class ActivityStreamBreadcrumb extends AbstractBreadcrumb {

	public ActivityStreamBreadcrumb( final Services services ) {
		super( services );
	}

	@Override
	public String getValue( final Language language ) {
		return getEntityLinkUtilsService().getActivityStreamRootLink( language );
	}
}
