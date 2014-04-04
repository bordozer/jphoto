package ui.services.breadcrumbs.items;

import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;

public class UserTeamLinkBreadcrumbs extends AbstractBreadcrumb {

	private User user;

	public UserTeamLinkBreadcrumbs( final User user, final Services services ) {
		super( services );
		this.user = user;
	}

	@Override
	public String getValue( final Language language ) {
		return getEntityLinkUtilsService().getUserTeamMemberListLink( user.getId(), language );
	}
}
