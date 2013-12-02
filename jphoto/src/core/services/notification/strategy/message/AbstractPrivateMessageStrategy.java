package core.services.notification.strategy.message;

import core.services.notification.strategy.AbstractNotificationStrategy;
import core.services.notification.strategy.NotificationData;
import core.services.security.Services;

import java.util.List;

public abstract class AbstractPrivateMessageStrategy extends AbstractNotificationStrategy {

	public AbstractPrivateMessageStrategy( final Services services ) {
		super( services );
	}

	@Override
	public final void sendNotifications() {

		final List<NotificationData> datas = getUsersSendNotificationTo();

		for ( final NotificationData data : datas ) {
			services.getPrivateMessageService().send( null, data.getUser(), data.getNotificationTextStrategy().getNotificationText() );
		}
	}
}
