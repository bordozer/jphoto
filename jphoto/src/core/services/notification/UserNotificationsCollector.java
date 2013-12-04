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

	public static List<UserNotification> newPhotoOfFriendPrivateMessage( final Photo photo, final Services services ) {
		return newPhotoOfFriendPrivateMessageStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> newPhotoOfSignedAuthorPrivateMessage( final Photo photo, final Services services ) {
		return newPhotoOfSignedAuthorPrivateMessageStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> newPhotoOfFavoriteAuthorEmail( final Photo photo, final Services services ) {
		return newPhotoOfFavoriteAuthorEmailStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> newPhotoOfFriendEmail( final Photo photo, final Services services ) {
		return newPhotoOfFriendEmailStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> newPhotoOfSignedAuthorEmail( final Photo photo, final Services services ) {
		return newPhotoOfSignedAuthorEmailStrategy( photo, services ).getUserNotifications();
	}

	/* All users get Private message about event */
	private static UserNotificationsCollector newPhotoOfFavoriteAuthorPrivateMessageStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return getNewPhotoUserNotificationForFavoriteType( photo, FavoriteEntryType.USER );
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String subject = String.format( "New Photo Of Favorite Author - Private Message Subject: %s has uploaded new photo '%s'", services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );
				final String message = String.format( "New Photo Of Favorite Author - Private Message Body: %s has uploaded new photo '%s'", services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );

				return new NotificationData( subject, message );
			}
		};
	}

	/* All users get Private message about event */
	private static UserNotificationsCollector newPhotoOfFriendPrivateMessageStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return getNewPhotoUserNotificationForFavoriteType( photo, FavoriteEntryType.FRIEND );
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String subject = String.format( "New Photo Of Friend - Private Message Subject: %s has uploaded new photo '%s'", services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );
				final String message = String.format( "New Photo Of Friend - Private Message Body: %s has uploaded new photo '%s'", services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );

				return new NotificationData( subject, message );
			}
		};
	}

	/* All users get Private message about event */
	private static UserNotificationsCollector newPhotoOfSignedAuthorPrivateMessageStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return getNewPhotoUserNotificationForFavoriteType( photo, FavoriteEntryType.NEW_PHOTO_NOTIFICATION );
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String subject = String.format( "New Photo Of Signed Author - Private Message Subject: %s has uploaded new photo '%s'", services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );
				final String message = String.format( "New Photo Of Signed Author - Private Message Body: %s has uploaded new photo '%s'", services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector newPhotoOfFavoriteAuthorEmailStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return filterByEmailNotificationType( newPhotoOfFavoriteAuthorPrivateMessageStrategy( photo, services ), EmailNotificationType.NEW_PHOTO_OF_FAVORITE_MEMBER );
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String subject = String.format( "New Photo Of Favorite Author - Email Subject: %s has uploaded new photo '%s'", services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );
				final String message = String.format( "New Photo Of Favorite Author - Email Body: %s has uploaded new photo '%s'", services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector newPhotoOfFriendEmailStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return filterByEmailNotificationType( newPhotoOfFriendPrivateMessageStrategy( photo, services ), EmailNotificationType.NEW_PHOTO_OF_FRIEND );
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String subject = String.format( "New Photo Of Friend - Email Subject: %s has uploaded new photo '%s'", services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );
				final String message = String.format( "New Photo Of Friend - Email Body: %s has uploaded new photo '%s'", services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector newPhotoOfSignedAuthorEmailStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return filterByEmailNotificationType( newPhotoOfFavoriteAuthorPrivateMessageStrategy( photo, services ), EmailNotificationType.NEW_PHOTO_OF_TRACKING_MEMBER );
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String subject = String.format( "New Photo Of Signed Author - Email Subject: %s has uploaded new photo '%s'", services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );
				final String message = String.format( "New Photo Of Signed Author - Email Body: %s has uploaded new photo '%s'", services.getEntityLinkUtilsService().getUserCardLink( photoAuthor ), getPhotoCardLink( photo ) );

				return new NotificationData( subject, message );
			}
		};
	}

	protected List<UserNotification> getNewPhotoUserNotificationForFavoriteType( final Photo photo, final FavoriteEntryType favoriteEntryType ) {
		final UserService userService = services.getUserService();

		final List<UserNotification> result = newArrayList();

		final List<Integer> notificationReceiverIds = services.getFavoritesService().getAllUsersIdsWhoHasThisEntryInFavorites( photo.getUserId(), favoriteEntryType );
		for ( final int userId : notificationReceiverIds ) {
			final User user = userService.load( userId );
			result.add( new UserNotification( user, AbstractSendNotificationStrategy.getSendPrivateMessageStrategy( services ), getNotificationData() ) );
		}

		return result;
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
