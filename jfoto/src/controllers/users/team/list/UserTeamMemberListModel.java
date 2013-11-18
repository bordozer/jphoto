package controllers.users.team.list;

import core.general.base.AbstractGeneralModel;
import core.general.user.User;
import core.general.user.userTeam.UserTeamMember;

import java.util.List;
import java.util.Map;

public class UserTeamMemberListModel extends AbstractGeneralModel {

	private User user;
	private List<UserTeamMember> userTeamMembers;
	private Map<Integer, Integer> userTeamMemberPhotosQtyMap;

	public void setUser( final User user ) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public List<UserTeamMember> getUserTeamMembers() {
		return userTeamMembers;
	}

	public void setUserTeamMembers( final List<UserTeamMember> userTeamMembers ) {
		this.userTeamMembers = userTeamMembers;
	}

	public Map<Integer, Integer> getUserTeamMemberPhotosQtyMap() {
		return userTeamMemberPhotosQtyMap;
	}

	public void setUserTeamMemberPhotosQtyMap( final Map<Integer, Integer> userTeamMemberPhotosQtyMap ) {
		this.userTeamMemberPhotosQtyMap = userTeamMemberPhotosQtyMap;
	}
}
