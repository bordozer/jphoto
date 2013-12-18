package core.services.notification;

import core.general.configuration.ConfigurationKey;
import core.services.mail.MailBean;
import core.services.security.Services;

public abstract class AbstractSendNotificationStrategy {

	public abstract void sendNotifications( final UserNotification userNotification, final Services services );

	public abstract SendStrategyType getStrategyType();

	public static final AbstractSendNotificationStrategy SEND_PRIVATE_MESSAGE_STRATEGY = new AbstractSendNotificationStrategy() {
		@Override
		public void sendNotifications( final UserNotification userNotification, final Services services ) {
			services.getPrivateMessageService().sendAdminMessage( userNotification.getUser(), userNotification.getNotificationData().getMessage() );
		}

		@Override
		public SendStrategyType getStrategyType() {
			return SendStrategyType.PRIVATE_MESSAGE;
		}
	};

	public static final AbstractSendNotificationStrategy SEND_EMAIL_STRATEGY = new AbstractSendNotificationStrategy() {
		@Override
		public void sendNotifications( final UserNotification userNotification, final Services services ) {
			final MailBean mail = new MailBean();

			mail.setToAddress( services.getConfigurationService().getString( ConfigurationKey.EMAILING_NO_REPLY_EMAIL ) ); // TODO: userNotification.getUser().getEmail()
			mail.setFromAddress( services.getConfigurationService().getString( ConfigurationKey.EMAILING_NO_REPLY_EMAIL ) );

			final NotificationData notificationData = userNotification.getNotificationData();

			mail.setSubject( notificationData.getSubject() );
			mail.setBody( notificationData.getMessage() );

			services.getMailService().sendNoException( mail );
		}

		@Override
		public SendStrategyType getStrategyType() {
			return SendStrategyType.EMAIL;
		}
	};
}
