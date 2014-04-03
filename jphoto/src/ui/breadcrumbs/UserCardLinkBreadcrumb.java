package ui.breadcrumbs;

import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;

public class UserCardLinkBreadcrumb extends AbstractBreadcrumb {

	private User user;

	public UserCardLinkBreadcrumb( final User user, final Services services ) {
		super( services );
		this.user = user;
	}

	@Override
	public String getValue( final Language language ) {
		return getEntityLinkUtilsService().getUserCardLink( user, language );
	}
}
