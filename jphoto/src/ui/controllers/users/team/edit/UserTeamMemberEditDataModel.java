package ui.controllers.users.team.edit;

import core.enums.UserTeamMemberType;
import core.general.base.AbstractGeneralModel;
import core.general.user.User;
import org.apache.commons.lang.StringUtils;

public class UserTeamMemberEditDataModel extends AbstractGeneralModel {

	public static final String FORM_CONTROL_TEAM_MEMBER_NAME = "teamMemberName";
	public static final String FORM_CONTROL_TEAM_MEMBER_USER_ID = "teamMemberUserId";
	public static final String FORM_CONTROL_TEAM_MEMBER_TYPE_ID = "teamMemberTypeId";

	private User user;

	private int userTeamMemberId;
	private String teamMemberName;
	private String teamMemberUserId;
	private User teamMemberUser;
	private UserTeamMemberType teamMemberType = UserTeamMemberType.MODEL;

	public void setUser( final User user ) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public int getUserTeamMemberId() {
		return userTeamMemberId;
	}

	public void setUserTeamMemberId( final int userTeamMemberId ) {
		this.userTeamMemberId = userTeamMemberId;
	}

	public String getTeamMemberName() {
		return teamMemberName;
	}

	public void setTeamMemberName( final String teamMemberName ) {
		this.teamMemberName = teamMemberName;
	}

	public User getTeamMemberUser() {
		return teamMemberUser;
	}

	public void setTeamMemberUser( final User teamMemberUser ) {
		this.teamMemberUser = teamMemberUser;
	}

	public String getTeamMemberUserId() {
		return teamMemberUserId;
	}

	public void setTeamMemberUserId( final String teamMemberUserId ) {
		this.teamMemberUserId = teamMemberUserId;
	}

	public UserTeamMemberType getTeamMemberType() {
		return teamMemberType;
	}

	public void setTeamMemberType( final UserTeamMemberType teamMemberType ) {
		this.teamMemberType = teamMemberType;
	}

	public int getTeamMemberTypeId() {
		return teamMemberType.getId();
	}

	public void setTeamMemberTypeId( final int teamMemberTypeId ) {
		teamMemberType = UserTeamMemberType.getById( teamMemberTypeId );
	}

	@Override
	public void clear() {
		super.clear();

		userTeamMemberId = 0;
		user = null;
		teamMemberName = StringUtils.EMPTY;
		teamMemberUserId = StringUtils.EMPTY;
		teamMemberUser = null;
		teamMemberType = UserTeamMemberType.MODEL;
	}
}
