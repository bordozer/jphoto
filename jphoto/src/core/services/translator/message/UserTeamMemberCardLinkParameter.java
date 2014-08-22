package core.services.translator.message;

import core.general.user.userTeam.UserTeamMember;
import core.services.system.Services;
import core.services.translator.Language;

public class UserTeamMemberCardLinkParameter extends AbstractTranslatableMessageParameter {

	private UserTeamMember userTeamMember;

	public UserTeamMemberCardLinkParameter( final UserTeamMember userTeamMember, final Services services ) {
		super( services );
		this.userTeamMember = userTeamMember;
	}

	@Override
	public String getValue( final Language language ) {
		return services.getEntityLinkUtilsService().getUserTeamMemberCardLink( userTeamMember, language );
	}
}
