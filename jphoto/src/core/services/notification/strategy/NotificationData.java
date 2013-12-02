package core.services.notification.strategy;

import core.general.user.User;
import core.services.notification.text.AbstractNotificationTextStrategy;

public class NotificationData {

	private final User user;
	private final AbstractNotificationTextStrategy notificationTextStrategy;

	public NotificationData( final User user, final AbstractNotificationTextStrategy notificationTextStrategy ) {
		this.user = user;
		this.notificationTextStrategy = notificationTextStrategy;
	}

	public User getUser() {
		return user;
	}

	public AbstractNotificationTextStrategy getNotificationTextStrategy() {
		return notificationTextStrategy;
	}
}
