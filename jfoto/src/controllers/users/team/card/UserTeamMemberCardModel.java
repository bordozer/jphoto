package controllers.users.team.card;

import core.general.base.AbstractGeneralModel;
import core.general.user.userTeam.UserTeamMember;
import elements.PhotoList;

public class UserTeamMemberCardModel extends AbstractGeneralModel {

	private UserTeamMember userTeamMember;
	private PhotoList photoList;

	public UserTeamMember getUserTeamMember() {
		return userTeamMember;
	}

	public void setUserTeamMember( final UserTeamMember userTeamMember ) {
		this.userTeamMember = userTeamMember;
	}

	public PhotoList getPhotoList() {
		return photoList;
	}

	public void setPhotoList( final PhotoList photoList ) {
		this.photoList = photoList;
	}
}
