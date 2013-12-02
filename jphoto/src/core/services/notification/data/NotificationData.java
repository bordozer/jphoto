package core.services.notification.data;

import core.general.user.User;
import core.services.notification.send.AbstractSendStrategy;
import core.services.notification.text.AbstractNotificationTextStrategy;

public class NotificationData {

	private final User user;
	private final AbstractNotificationTextStrategy notificationTextStrategy;
	private final AbstractSendStrategy sendStrategy;

	public NotificationData( final User user, final AbstractNotificationTextStrategy notificationTextStrategy, final AbstractSendStrategy sendStrategy ) {
		this.user = user;
		this.notificationTextStrategy = notificationTextStrategy;
		this.sendStrategy = sendStrategy;
	}

	public User getUser() {
		return user;
	}

	public AbstractNotificationTextStrategy getNotificationTextStrategy() {
		return notificationTextStrategy;
	}

	public AbstractSendStrategy getSendStrategy() {
		return sendStrategy;
	}

	public void sendNotification() {
		getSendStrategy().sendNotifications( this );
	}

	@Override
	public String toString() {
		return String.format( "%s - Notification Data", user );
	}
}
