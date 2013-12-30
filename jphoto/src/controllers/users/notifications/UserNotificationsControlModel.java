package controllers.users.notifications;

import core.general.base.AbstractGeneralModel;
import core.general.user.User;

public class UserNotificationsControlModel extends AbstractGeneralModel {

	private User user;

	public void setUser( final User user ) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
