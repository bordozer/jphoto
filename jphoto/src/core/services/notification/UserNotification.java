package core.services.notification;

import core.general.user.User;

public class UserNotification {

	private final User user;
	private final AbstractSendNotificationStrategy sendNotificationStrategy;
	private final NotificationData notificationData;

	public UserNotification( final User user, final AbstractSendNotificationStrategy sendNotificationStrategy, final NotificationData notificationData ) {
		this.user = user;
		this.sendNotificationStrategy = sendNotificationStrategy;
		this.notificationData = notificationData;
	}

	public User getUser() {
		return user;
	}

	public AbstractSendNotificationStrategy getSendNotificationStrategy() {
		return sendNotificationStrategy;
	}

	public NotificationData getNotificationData() {
		return notificationData;
	}

	@Override
	public String toString() {
		return String.format( "%s - Notification Data", user );
	}

	public void sendNotifications() {
		sendNotificationStrategy.sendNotifications( this );
	}
}
