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

	public abstract AbstractSendNotificationStrategy getSendNotificationStrategy();

	public abstract NotificationData getNotificationData();

	public static List<UserNotification> newPhotoOfFavoriteMemberPrivateMessage( final Photo photo, final Services services ) {
		return newPhotoOfFavoriteMemberPrivateMessageStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> newPhotoOfFriendPrivateMessage( final Photo photo, final Services services ) {
		return newPhotoOfFriendPrivateMessageStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> newPhotoOfSignedMemberPrivateMessage( final Photo photo, final Services services ) {
		return newPhotoOfSignedMemberPrivateMessageStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> newPhotoOfFavoriteMemberEmail( final Photo photo, final Services services ) {
		return newPhotoOfFavoriteMemberEmailStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> newPhotoOfFriendEmail( final Photo photo, final Services services ) {
		return newPhotoOfFriendEmailStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> newPhotoOfSignedMemberEmail( final Photo photo, final Services services ) {
		return newPhotoOfSignedMemberEmailStrategy( photo, services ).getUserNotifications();
	}

	/* All users get Private message about event */
	private static UserNotificationsCollector newPhotoOfFavoriteMemberPrivateMessageStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return getNewPhotoUserNotificationForFavoriteType( photo, FavoriteEntryType.USER );
			}

			@Override
			public AbstractSendNotificationStrategy getSendNotificationStrategy() {
				return AbstractSendNotificationStrategy.SEND_PRIVATE_MESSAGE_STRATEGY;
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String userCardLink = getUserCardLink( photoAuthor );
				final String photoCardLink = getPhotoCardLink( photo );

				final String subject = String.format( "New Photo Of Favorite members - Private Message Subject: One of your favorite members %s has uploaded new photo '%s'", userCardLink, photoCardLink );
				final String message = String.format( "New Photo Of Favorite members - Private Message Body: One of your favorite members %s has uploaded new photo '%s'", userCardLink, photoCardLink );

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
			public AbstractSendNotificationStrategy getSendNotificationStrategy() {
				return AbstractSendNotificationStrategy.SEND_PRIVATE_MESSAGE_STRATEGY;
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String userCardLink = getUserCardLink( photoAuthor );
				final String photoCardLink = getPhotoCardLink( photo );

				final String subject = String.format( "New Photo Of Friend - Private Message Subject: One of your friends %s has uploaded new photo '%s'", userCardLink, photoCardLink );
				final String message = String.format( "New Photo Of Friend - Private Message Body: One of your friends %s has uploaded new photo '%s'", userCardLink, photoCardLink );

				return new NotificationData( subject, message );
			}
		};
	}

	/* All users get Private message about event */
	private static UserNotificationsCollector newPhotoOfSignedMemberPrivateMessageStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return getNewPhotoUserNotificationForFavoriteType( photo, FavoriteEntryType.NEW_PHOTO_NOTIFICATION );
			}

			@Override
			public AbstractSendNotificationStrategy getSendNotificationStrategy() {
				return AbstractSendNotificationStrategy.SEND_PRIVATE_MESSAGE_STRATEGY;
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String userCardLink = getUserCardLink( photoAuthor );
				final String photoCardLink = getPhotoCardLink( photo );

				final String subject = String.format( "New Photo Of Signed members - Private Message Subject: %s has uploaded new photo '%s'", userCardLink, photoCardLink );
				final String message = String.format( "New Photo Of Signed members - Private Message Body: %s has uploaded new photo '%s'."
													  + " You signed on new photos of this member.", userCardLink, photoCardLink );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector newPhotoOfFavoriteMemberEmailStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return filterByEmailNotificationType( newPhotoOfFavoriteMemberPrivateMessageStrategy( photo, services ), EmailNotificationType.NEW_PHOTO_OF_FAVORITE_MEMBER );
			}

			@Override
			public AbstractSendNotificationStrategy getSendNotificationStrategy() {
				return AbstractSendNotificationStrategy.SEND_EMAIL_STRATEGY;
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String userCardLink = getUserCardLink( photoAuthor );
				final String photoCardLink = getPhotoCardLink( photo );

				final String subject = String.format( "New Photo Of Favorite member - Email Subject: One of your favorite member %s has uploaded new photo '%s'", userCardLink, photoCardLink );
				final String message = String.format( "New Photo Of Favorite member - Email Body: One of your favorite member %s has uploaded new photo '%s'", userCardLink, photoCardLink );

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
			public AbstractSendNotificationStrategy getSendNotificationStrategy() {
				return AbstractSendNotificationStrategy.SEND_EMAIL_STRATEGY;
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String userCardLink = getUserCardLink( photoAuthor );
				final String photoCardLink = getPhotoCardLink( photo );

				final String subject = String.format( "New Photo Of Friend - Email Subject: One of your friend %s has uploaded new photo '%s'", userCardLink, photoCardLink );
				final String message = String.format( "New Photo Of Friend - Email Body: One of your friend %s has uploaded new photo '%s'", userCardLink, photoCardLink );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector newPhotoOfSignedMemberEmailStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return filterByEmailNotificationType( newPhotoOfFavoriteMemberPrivateMessageStrategy( photo, services ), EmailNotificationType.NEW_PHOTO_OF_TRACKING_MEMBER );
			}

			@Override
			public AbstractSendNotificationStrategy getSendNotificationStrategy() {
				return AbstractSendNotificationStrategy.SEND_EMAIL_STRATEGY;
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String userCardLink = getUserCardLink( photoAuthor );
				final String photoCardLink = getPhotoCardLink( photo );

				final String subject = String.format( "New Photo Of Signed members - Email Subject: %s has uploaded new photo '%s'", userCardLink, photoCardLink );
				final String message = String.format( "New Photo Of Signed members - Email Body: %s has uploaded new photo '%s'. You signed on new photos of this member.", userCardLink, photoCardLink );

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
			result.add( new UserNotification( user, getSendNotificationStrategy(), getNotificationData() ) );
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

	protected User getPhotoAuthor( final Photo photo ) {
		return services.getUserService().load( photo.getUserId() );
	}

	protected String getPhotoCardLink( final Photo photo ) {
		return services.getEntityLinkUtilsService().getPhotoCardLink( photo );
	}

	protected String getUserCardLink( final User photoAuthor ) {
		return services.getEntityLinkUtilsService().getUserCardLink( photoAuthor );
	}
}
