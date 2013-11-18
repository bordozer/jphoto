package core.general.user.userTeam;

import core.general.user.User;

import java.util.List;

public class UserTeam {

	private final User user;
	private final List<UserTeamMember> userTeamMembers;

	public UserTeam( final User user, final List<UserTeamMember> userTeamMembers ) {
		this.user = user;
		this.userTeamMembers = userTeamMembers;
	}

	public User getUser() {
		return user;
	}

	public List<UserTeamMember> getUserTeamMembers() {
		return userTeamMembers;
	}
}
