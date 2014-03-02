package controllers.users.notifications;

import core.general.base.AbstractGeneralModel;
import core.general.user.User;

import java.util.Set;

public class UserNotificationsControlModel extends AbstractGeneralModel {

	public static final String EMAIL_NOTIFICATION_TYPE_IDS_FORM_CONTROL = "emailNotificationTypeIds";

	private User user;

	private Set<String> emailNotificationTypeIds;

	public void setUser( final User user ) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public Set<String> getEmailNotificationTypeIds() {
		return emailNotificationTypeIds;
	}

	public void setEmailNotificationTypeIds( final Set<String> emailNotificationTypeIds ) {
		this.emailNotificationTypeIds = emailNotificationTypeIds;
	}
}
