package core.services.notification.send;

import core.enums.PrivateMessageType;
import core.services.notification.data.NotificationData;
import core.services.security.Services;

public class SendPrivateMessageStrategy extends AbstractSendStrategy {

	public SendPrivateMessageStrategy( final Services services ) {
		super( services );
	}

	@Override
	public final void sendNotifications( final NotificationData data ) {
		services.getPrivateMessageService().send( null, data.getUser(), PrivateMessageType.SYSTEM_INFORMATION, data.getNotificationTextStrategy().getNotificationText() );
	}
}
