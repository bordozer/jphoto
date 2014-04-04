package ui.services.breadcrumbs.items;

import core.general.user.userTeam.UserTeamMember;
import core.services.system.Services;
import core.services.translator.Language;

public class UserTeamMemberNameBreadcrumbs extends AbstractBreadcrumb {

	private UserTeamMember userTeamMember;

	public UserTeamMemberNameBreadcrumbs( final UserTeamMember userTeamMember, final Services services ) {
		super( services );
		this.userTeamMember = userTeamMember;
	}

	@Override
	public String getValue( final Language language ) {
		return userTeamMember.getTeamMemberName();
	}
}
