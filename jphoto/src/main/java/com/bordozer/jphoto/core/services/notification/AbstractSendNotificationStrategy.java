package com.bordozer.jphoto.core.services.notification;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.services.mail.MailBean;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public abstract class AbstractSendNotificationStrategy {

    public abstract void sendNotifications(final UserNotification userNotification, final Services services);

    public abstract SendStrategyType getStrategyType();

    public static final AbstractSendNotificationStrategy SEND_PRIVATE_MESSAGE_STRATEGY = new AbstractSendNotificationStrategy() {
        @Override
        public void sendNotifications(final UserNotification userNotification, final Services services) {
            services.getPrivateMessageService().sendActivityNotificationMessage(userNotification.getUser(), userNotification.getNotificationData().getMessage().build(userNotification.getUser().getLanguage()));
        }

        @Override
        public SendStrategyType getStrategyType() {
            return SendStrategyType.PRIVATE_MESSAGE;
        }
    };

    public static final AbstractSendNotificationStrategy SEND_EMAIL_STRATEGY = new AbstractSendNotificationStrategy() {
        @Override
        public void sendNotifications(final UserNotification userNotification, final Services services) {
            final MailBean mail = new MailBean();

            mail.setToAddress(services.getConfigurationService().getString(ConfigurationKey.EMAILING_NO_REPLY_EMAIL)); // TODO: userNotification.getUser().getEmail()
            mail.setFromAddress(services.getConfigurationService().getString(ConfigurationKey.EMAILING_NO_REPLY_EMAIL));

            final NotificationData notificationData = userNotification.getNotificationData();

            final Language language = userNotification.getUser().getLanguage();

            mail.setSubject(notificationData.getSubject().build(language));
            mail.setBody(notificationData.getMessage().build(language));

            services.getMailService().sendNoException(mail);
        }

        @Override
        public SendStrategyType getStrategyType() {
            return SendStrategyType.EMAIL;
        }
    };
}
