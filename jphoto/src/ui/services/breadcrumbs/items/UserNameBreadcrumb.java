package ui.services.breadcrumbs.items;

import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;

public class UserNameBreadcrumb extends AbstractBreadcrumb {

	private User user;

	public UserNameBreadcrumb( final User user, final Services services ) {
		super( services );
		this.user = user;
	}

	@Override
	public String getValue( final Language language ) {
		return user.getNameEscaped();
	}
}
