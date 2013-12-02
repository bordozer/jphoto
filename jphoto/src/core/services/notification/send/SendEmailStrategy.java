package core.services.notification.send;

import core.general.user.User;
import core.services.mail.MailBean;
import core.services.notification.data.NotificationData;
import core.services.notification.text.AbstractNotificationTextStrategy;
import core.services.security.Services;

public class SendEmailStrategy extends AbstractSendStrategy {

	public SendEmailStrategy( final Services services ) {
		super( services );
	}

	@Override
	public final void sendNotifications( final NotificationData data ) {

		final User user = data.getUser();
		final AbstractNotificationTextStrategy notificationTextStrategy = data.getNotificationTextStrategy();

		final MailBean mail = new MailBean();

		mail.setToAddress( services.getSystemVarsService().getEmailNoReply() ); // TODO: user.getEmail()
		mail.setFromAddress( services.getSystemVarsService().getEmailNoReply() );

		mail.setSubject( notificationTextStrategy.getNotificationSubject() );
		mail.setBody( notificationTextStrategy.getNotificationText() );

		services.getMailService().sendNoException( mail );
	}
}
