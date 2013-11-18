package core.general.photoTeam;

import core.general.user.userTeam.UserTeamMember;

public class PhotoTeamMember {

	private UserTeamMember userTeamMember;
	private String description;

	public UserTeamMember getUserTeamMember() {
		return userTeamMember;
	}

	public void setUserTeamMember( final UserTeamMember userTeamMember ) {
		this.userTeamMember = userTeamMember;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( final String description ) {
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format( "%s", userTeamMember );
	}
}
