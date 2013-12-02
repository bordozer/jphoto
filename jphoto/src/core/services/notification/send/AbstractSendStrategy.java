package core.services.notification.send;

import core.services.notification.data.NotificationData;
import core.services.security.Services;

public abstract class AbstractSendStrategy {

	protected final Services services;

	protected AbstractSendStrategy( final Services services ) {
		this.services = services;
	}

	public abstract void sendNotifications( final NotificationData data );
}
