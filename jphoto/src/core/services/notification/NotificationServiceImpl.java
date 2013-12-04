package core.services.notification;

import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.services.security.Services;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private Services services;

	@Override
	public void newPhotoNotification( final Photo photo ) {

		final List<UserNotification> userNotifications = newArrayList();

		userNotifications.addAll( UserNotificationsCollector.newPhotoOfFavoriteAuthorPrivateMessage( photo, services ) );
		userNotifications.addAll( UserNotificationsCollector.newPhotoOfFavoriteAuthorEmail( photo, services ) );
		userNotifications.addAll( UserNotificationsCollector.newPhotoOfSignedAuthorPrivateMessage( photo, services ) );
		userNotifications.addAll( UserNotificationsCollector.newPhotoOfSignedAuthorEmail( photo, services ) );

		for ( final UserNotification userNotification : userNotifications ) {
			userNotification.sendNotifications();
		}
	}

	@Override
	public void newCommentNotification( final PhotoComment comment ) {

	}
}
