package core.services.notification.data;

import core.log.LogHelper;
import core.services.notification.send.AbstractSendStrategy;
import core.services.security.Services;

import java.util.List;

public abstract class AbstractNotificationDataHolder {

	protected Services services;

	protected final LogHelper log = new LogHelper( this.getClass() );

	public AbstractNotificationDataHolder( final Services services ) {
		this.services = services;
	}

	public abstract List<NotificationData> getNotificationsData( final AbstractSendStrategy sendStrategy );
}
