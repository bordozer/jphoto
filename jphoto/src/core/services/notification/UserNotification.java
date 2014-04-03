package core.services.notification;

import core.general.user.User;
import core.services.system.Services;

public class UserNotification {

	private final User user;
	private AbstractSendNotificationStrategy sendNotificationStrategy;
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

	public void sendNotifications( final Services services ) { 
		sendNotificationStrategy.sendNotifications( this, services );
	}

	@Override
	public int hashCode() {
		return user.hashCode() + sendNotificationStrategy.getStrategyType().hashCode();
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof UserNotification ) ) {
			return false;
		}

		final UserNotification notification = ( UserNotification ) obj;
		return notification.getUser().getId() == user.getId() && notification.getSendNotificationStrategy().equals( sendNotificationStrategy );
	}

	@Override
	public String toString() {
		return String.format( "%s to %s", sendNotificationStrategy.getStrategyType(), user );
	}
}
