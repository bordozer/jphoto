package core.services.notification;

import core.general.message.PrivateMessage;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.services.security.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private Services services;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public void newPhotoNotification( final Photo photo ) {

		final Set<UserNotification> userNotifications = newLinkedHashSet();

		userNotifications.addAll( UserNotificationsCollector.privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFavorites( photo, services ) );
		userNotifications.addAll( UserNotificationsCollector.privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFriends( photo, services ) );
		userNotifications.addAll( UserNotificationsCollector.privateMessagesAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthor( photo, services ) );

		userNotifications.addAll( UserNotificationsCollector.emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFavorites( photo, services ) );
		userNotifications.addAll( UserNotificationsCollector.emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFriends( photo, services ) );
		userNotifications.addAll( UserNotificationsCollector.emailsAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthor( photo, services ) );

		sendNotifications( userNotifications );
	}

	@Override
	public void newCommentNotification( final PhotoComment comment ) {
		final Set<UserNotification> userNotifications = newLinkedHashSet();

		userNotifications.addAll( UserNotificationsCollector.privateMessagesAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhoto( comment, services ) );

		userNotifications.addAll( UserNotificationsCollector.emailToPhotoAuthorAboutNewCommentToHisPhoto( comment, services ) );
		userNotifications.addAll( UserNotificationsCollector.emailsAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhoto( comment, services ) );

		sendNotifications( userNotifications );
	}

	@Override
	public void newPrivateMessage( final PrivateMessage privateMessage ) {
		final Language language = privateMessage.getToUser().getLanguage();

		final String subject = translatorService.translate( "New private message", language );
		final String message = translatorService.translate( "$1 has sent you a private message", language, privateMessage.getFromUser().getNameEscaped() );
		final NotificationData data = new NotificationData( subject, message );

		final UserNotification userNotification = new UserNotification( privateMessage.getToUser(), AbstractSendNotificationStrategy.SEND_EMAIL_STRATEGY, data );

		userNotification.sendNotifications( services );
	}

	private void sendNotifications( final Set<UserNotification> userNotifications ) {
		for ( final UserNotification userNotification : userNotifications ) {
			userNotification.sendNotifications( services );
		}
	}
}
