package core.services.notification.send;

import core.services.notification.data.NotificationData;
import core.services.security.Services;

public class SendPrivateMessageStrategy extends AbstractSendStrategy {

	public SendPrivateMessageStrategy( final Services services ) {
		super( services );
	}

	@Override
	public final void sendNotifications( final NotificationData data ) {
		services.getPrivateMessageService().send( null, data.getUser(), data.getNotificationTextStrategy().getNotificationText() );
	}
}
