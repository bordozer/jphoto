package ui.services.breadcrumbs.items;

import core.services.system.Services;
import core.services.translator.Language;

public class UserListLinkBreadcrumbs extends AbstractBreadcrumb {

	public UserListLinkBreadcrumbs( final Services services ) {
		super( services );
	}

	@Override
	public String getValue( final Language language ) {
		return getEntityLinkUtilsService().getUsersRootLink( language );
	}
}
