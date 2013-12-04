package core.services.notification;

import core.enums.PrivateMessageType;
import core.services.mail.MailBean;
import core.services.security.Services;

public abstract class AbstractSendNotificationStrategy {

	protected final Services services;

	protected AbstractSendNotificationStrategy( final Services services ) {
		this.services = services;
	}

	public abstract void sendNotifications( final UserNotification userNotification, final NotificationData notificationData );

	public static AbstractSendNotificationStrategy getSendEmailStrategy( final Services services ) {
		return new AbstractSendNotificationStrategy( services ) {
			@Override
			public void sendNotifications( final UserNotification userNotification, final NotificationData notificationData ) {
				final MailBean mail = new MailBean();

				mail.setToAddress( services.getSystemVarsService().getEmailNoReply() ); // TODO: userNotification.getUser().getEmail()
				mail.setFromAddress( services.getSystemVarsService().getEmailNoReply() );

				mail.setSubject( notificationData.getSubject() );
				mail.setBody( notificationData.getMessage() );

				services.getMailService().sendNoException( mail );
			}
		};
	}

	public static AbstractSendNotificationStrategy getSendPrivateMessageStrategy( final Services services ) {
		return new AbstractSendNotificationStrategy( services ) {
			@Override
			public void sendNotifications( final UserNotification userNotification, final NotificationData notificationData ) {
				services.getPrivateMessageService().send( null, userNotification.getUser(), PrivateMessageType.SYSTEM_INFORMATION, notificationData.getMessage() );
			}
		};
	}
}
