package core.services.notification.strategy.email;

import core.general.user.User;
import core.services.mail.MailBean;
import core.services.notification.strategy.AbstractNotificationStrategy;
import core.services.notification.strategy.NotificationData;
import core.services.notification.text.AbstractNotificationTextStrategy;
import core.services.security.Services;

import java.util.List;

public abstract class AbstractEmailStrategy extends AbstractNotificationStrategy {

	public AbstractEmailStrategy( final Services services ) {
		super( services );
	}

	@Override
	public final void sendNotifications() {
		final List<NotificationData> datas = getUsersSendNotificationTo();

		for ( final NotificationData data : datas ) {
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
}
