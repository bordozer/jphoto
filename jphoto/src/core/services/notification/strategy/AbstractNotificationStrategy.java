package core.services.notification.strategy;

import core.general.user.User;
import core.log.LogHelper;
import core.services.notification.text.AbstractNotificationTextStrategy;
import core.services.security.Services;

import java.util.List;

public abstract class AbstractNotificationStrategy {

	protected Services services;

	protected final LogHelper log = new LogHelper( this.getClass() );

	public AbstractNotificationStrategy( final Services services ) {
		this.services = services;
	}

	public abstract void sendNotifications();

	protected abstract List<NotificationData> getUsersSendNotificationTo();
}
