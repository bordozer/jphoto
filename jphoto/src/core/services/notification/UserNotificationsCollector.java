package core.services.notification;

import core.enums.FavoriteEntryType;
import core.general.photo.Photo;
import core.general.user.EmailNotificationType;
import core.general.user.User;
import core.log.LogHelper;
import core.services.security.Services;
import core.services.user.UserService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class UserNotificationsCollector {

	protected Services services;

	protected final LogHelper log = new LogHelper( this.getClass() );

	public UserNotificationsCollector( final Services services ) {
		this.services = services;
	}

	public abstract List<UserNotification> getUserNotifications();

	public abstract NotificationData getNotificationData();

	public static List<UserNotification> newPhotoOfFavoriteAuthorPrivateMessage( final Photo photo, final Services services ) {
		return newPhotoOfFavoriteAuthorPrivateMessageStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> newPhotoOfFavoriteAuthorEmail( final Photo photo, final Services services ) {
		return newPhotoOfFavoriteAuthorEmailStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> newPhotoOfSignedAuthorPrivateMessage( final Photo photo, final Services services ) {
		return newPhotoOfSignedAuthorPrivateMessageStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> newPhotoOfSignedAuthorEmail( final Photo photo, final Services services ) {
		return newPhotoOfSignedAuthorEmailStrategy( photo, services ).getUserNotifications();
	}

	/* All users get Private message about event */
	private static UserNotificationsCollector newPhotoOfFavoriteAuthorPrivateMessageStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {
			@Override
			public List<UserNotification> getUserNotifications() {
				final UserService userService = services.getUserService();

				final List<UserNotification> result = newArrayList();

				final List<Integer> usersWhoHasPhotoAuthorInFavorites = services.getFavoritesService().getAllUsersIdsWhoHasThisEntryInFavorites( photo.getUserId(), FavoriteEntryType.USER );
				for ( final int userId : usersWhoHasPhotoAuthorInFavorites ) {
					final User user = userService.load( userId );
					result.add( new UserNotification( user, AbstractSendNotificationStrategy.getSendPrivateMessageStrategy( services ), getNotificationData() ) );
				}

				return result;
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String subject = String.format( "New Photo Of Favorite Author - Private Message Subject: %s has uploaded new photo '%s'"
					, services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );
				final String message = String.format( "New Photo Of Favorite Author - Private Message Body: %s has uploaded new photo '%s'"
					, services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );

				return new NotificationData( subject, message );
			}
		};
	}

	/* All users get Private message about event */
	private static UserNotificationsCollector newPhotoOfSignedAuthorPrivateMessageStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {
			@Override
			public List<UserNotification> getUserNotifications() {
				final UserService userService = services.getUserService();

				final List<UserNotification> result = newArrayList();

				final List<Integer> usersWhoSignedOnGettingNotificationAboutNewPhoto = services.getFavoritesService().getAllUsersIdsWhoHasThisEntryInFavorites( photo.getUserId(), FavoriteEntryType.NEW_PHOTO_NOTIFICATION );
				for ( final int userId : usersWhoSignedOnGettingNotificationAboutNewPhoto ) {
					final User user = userService.load( userId );
					result.add( new UserNotification( user, AbstractSendNotificationStrategy.getSendPrivateMessageStrategy( services ), getNotificationData() ) );
				}

				return result;
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String subject = String.format( "New Photo Of Signed Author - Private Message Subject: %s has uploaded new photo '%s'"
					, services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );
				final String message = String.format( "New Photo Of Signed Author - Private Message Body: %s has uploaded new photo '%s'"
					, services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector newPhotoOfFavoriteAuthorEmailStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return filterByEmailNotificationType( newPhotoOfFavoriteAuthorPrivateMessageStrategy( photo, services ), EmailNotificationType.PHOTO_OF_FAVORITE_MEMBER );
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String subject = String.format( "New Photo Of Favorite Author - Email Subject: %s has uploaded new photo '%s'"
					, services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );
				final String message = String.format( "New Photo Of Favorite Author - Email Body: %s has uploaded new photo '%s'"
					, services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector newPhotoOfSignedAuthorEmailStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {
			@Override
			public List<UserNotification> getUserNotifications() {
				return filterByEmailNotificationType( newPhotoOfFavoriteAuthorPrivateMessageStrategy( photo, services ), EmailNotificationType.PHOTO_OF_TRACKING_MEMBER );
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String subject = String.format( "New Photo Of Signed Author - Email Subject: %s has uploaded new photo '%s'"
					, services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );
				final String message = String.format( "New Photo Of Signed Author - Email Body: %s has uploaded new photo '%s'"
					, services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );

				return new NotificationData( subject, message );
			}
		};
	}

	private static List<UserNotification> filterByEmailNotificationType( final UserNotificationsCollector collector, final EmailNotificationType emailNotificationType ) {

		final List<UserNotification> result = collector.getUserNotifications();

		CollectionUtils.filter( result, new Predicate<UserNotification>() {
			@Override
			public boolean evaluate( final UserNotification userNotification ) {
				return userNotification.getUser().getEmailNotificationTypes().contains( emailNotificationType );
			}
		} );

		return result;
	}

	protected String getPhotoCardLink( final Photo photo ) {
		return services.getEntityLinkUtilsService().getPhotoCardLink( photo );
	}

	protected User getPhotoAuthor( final Photo photo ) {
		return services.getUserService().load( photo.getUserId() );
	}
}
