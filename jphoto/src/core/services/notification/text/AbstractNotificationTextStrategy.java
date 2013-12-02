package core.services.notification.text;

import core.services.security.Services;

public abstract class AbstractNotificationTextStrategy {

	protected final Services services;

	protected AbstractNotificationTextStrategy( final Services services ) {
		this.services = services;
	}

	public abstract String getNotificationSubject();

	public abstract String getNotificationText();
}
