package core.services.notification;

import core.enums.FavoriteEntryType;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
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

	public static List<UserNotification> privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFavorites( final Photo photo, final Services services ) {
		return privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFriends( final Photo photo, final Services services ) {
		return privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFriendsStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> privateMessagesAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthor( final Photo photo, final Services services ) {
		return privateMessagesAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthorStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFavorites( final Photo photo, final Services services ) {
		return emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFriends( final Photo photo, final Services services ) {
		return emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFriendsStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> emailsAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthor( final Photo photo, final Services services ) {
		return emailsAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthorStrategy( photo, services ).getUserNotifications();
	}

	public static List<UserNotification> emailToPhotoAuthorAboutNewCommentToHisPhoto( final PhotoComment comment, final Services services ) {
		return emailToPhotoAuthorAboutNewCommentToHisPhotoStrategy( comment, services ).getUserNotifications();
	}

	public static List<UserNotification> privateMessagesAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhoto( final PhotoComment comment, final Services services ) {
		return privateMessagesAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhotoStrategy( comment, services ).getUserNotifications();
	}

	public static List<UserNotification> emailsAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhoto( final PhotoComment comment, final Services services ) {
		return emailsAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhotoStrategy( comment, services ).getUserNotifications();
	}

	/* All users get Private message about event */
	private static UserNotificationsCollector privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return getNewPhotoUserNotificationForFavoriteType( FavoriteEntryType.USER, photo.getUserId() );
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

				final String subject = String.format( "New Photo Of Favorite members - Private Message Subject: One of your favorite members %s has uploaded new photo '%s'"
					, photoAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "New Photo Of Favorite members - Private Message Body: One of your favorite members %s has uploaded new photo '%s'"
					, userCardLink, photoCardLink );

				return new NotificationData( subject, message );
			}
		};
	}

	/* All users get Private message about event */
	private static UserNotificationsCollector privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFriendsStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return getNewPhotoUserNotificationForFavoriteType( FavoriteEntryType.FRIEND, photo.getUserId() );
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

				final String subject = String.format( "New Photo Of Friend - Private Message Subject: One of your friends %s has uploaded new photo '%s'"
					, photoAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "New Photo Of Friend - Private Message Body: One of your friends %s has uploaded new photo '%s'"
					, userCardLink, photoCardLink );

				return new NotificationData( subject, message );
			}
		};
	}

	/* All users get Private message about event */
	private static UserNotificationsCollector privateMessagesAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthorStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return getNewPhotoUserNotificationForFavoriteType( FavoriteEntryType.NEW_PHOTO_NOTIFICATION, photo.getUserId() );
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

				final String subject = String.format( "New Photo Of Signed members - Private Message Subject: %s has uploaded new photo '%s'"
					, photoAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "New Photo Of Signed members - Private Message Body: %s has uploaded new photo '%s'."
													  + " You signed on new photos of this member.", userCardLink, photoCardLink );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return filterByEmailNotificationType( privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy( photo, services ), EmailNotificationType.NEW_PHOTO_OF_FAVORITE_MEMBER );
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

				final String subject = String.format( "New Photo Of Favorite member - Email Subject: One of your favorite member %s has uploaded new photo '%s'"
					, photoAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "New Photo Of Favorite member - Email Body: One of your favorite member %s has uploaded new photo '%s'"
					, userCardLink, photoCardLink );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFriendsStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return filterByEmailNotificationType( privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFriendsStrategy( photo, services ), EmailNotificationType.NEW_PHOTO_OF_FRIEND );
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

				final String subject = String.format( "New Photo Of Friend - Email Subject: One of your friend %s has uploaded new photo '%s'"
					, photoAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "New Photo Of Friend - Email Body: One of your friend %s has uploaded new photo '%s'"
					, userCardLink, photoCardLink );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector emailsAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthorStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return filterByEmailNotificationType( privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy( photo, services ), EmailNotificationType.NEW_PHOTO_OF_TRACKING_MEMBER );
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

				final String subject = String.format( "New Photo Of Signed members - Email Subject: %s has uploaded new photo '%s'"
					, photoAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "New Photo Of Signed members - Email Body: %s has uploaded new photo '%s'. You signed on new photos of this member."
					, userCardLink, photoCardLink );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector emailToPhotoAuthorAboutNewCommentToHisPhotoStrategy( final PhotoComment comment, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return newArrayList( new UserNotification( comment.getCommentAuthor(), getSendNotificationStrategy(), getNotificationData() ) );
			}

			@Override
			public AbstractSendNotificationStrategy getSendNotificationStrategy() {
				return AbstractSendNotificationStrategy.SEND_EMAIL_STRATEGY;
			}

			@Override
			public NotificationData getNotificationData() {
				final User commentAuthor = comment.getCommentAuthor();

				final String commentAuthorCardLink = getUserCardLink( commentAuthor );

				final Photo photo = services.getPhotoService().load( comment.getPhotoId() );
				final String photoCardLink = getPhotoCardLink( photo );

				final String subject = String.format( "New comment to your photo - Subject: %s has commented your photo '%s'"
					, commentAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "New comment to your photo - Body: %s has commented your photo '%s'"
					, commentAuthorCardLink, photoCardLink );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector privateMessagesAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhotoStrategy( final PhotoComment comment, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return getNewPhotoUserNotificationForFavoriteType( FavoriteEntryType.NEW_COMMENTS_NOTIFICATION, comment.getPhotoId() );
			}

			@Override
			public AbstractSendNotificationStrategy getSendNotificationStrategy() {
				return AbstractSendNotificationStrategy.SEND_PRIVATE_MESSAGE_STRATEGY;
			}

			@Override
			public NotificationData getNotificationData() {
				final User commentAuthor = comment.getCommentAuthor();

				final String commentAuthorCardLink = getUserCardLink( commentAuthor );

				final Photo photo = services.getPhotoService().load( comment.getPhotoId() );
				final String photoCardLink = getPhotoCardLink( photo );

				final String subject = String.format( "New comment to photo you are tracking comments of - Private message Subject: %s has commented photo '%s'."
					, commentAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "New comment to photo you are tracking comments of - Private message Body: %s has commented photo '%s'."
													  + " You got this comment because you are tracking new comments to the photo"
					, commentAuthorCardLink, photoCardLink );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector emailsAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhotoStrategy( final PhotoComment comment, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return filterByEmailNotificationType( privateMessagesAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhotoStrategy( comment, services ), EmailNotificationType.COMMENT_TO_TRACKING_PHOTO );
			}

			@Override
			public AbstractSendNotificationStrategy getSendNotificationStrategy() {
				return AbstractSendNotificationStrategy.SEND_EMAIL_STRATEGY;
			}

			@Override
			public NotificationData getNotificationData() {
				final User commentAuthor = comment.getCommentAuthor();

				final String commentAuthorCardLink = getUserCardLink( commentAuthor );

				final Photo photo = services.getPhotoService().load( comment.getPhotoId() );
				final String photoCardLink = getPhotoCardLink( photo );

				final String subject = String.format( "New comment to photo you are tracking comments of - Email Subject: %s has commented photo '%s'"
					, commentAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "New comment to photo you are tracking comments of - Email Body: %s has commented photo '%s'"
													  + " You got this comment because you are tracking new comments to the photo"
					, commentAuthorCardLink, photoCardLink );

				return new NotificationData( subject, message );
			}
		};
	}

	protected List<UserNotification> getNewPhotoUserNotificationForFavoriteType( final FavoriteEntryType favoriteEntryType, final int userId ) {
		final UserService userService = services.getUserService();

		final List<UserNotification> result = newArrayList();

		final List<Integer> notificationReceiverIds = services.getFavoritesService().getAllUsersIdsWhoHasThisEntryInFavorites( userId, favoriteEntryType );
		for ( final int userIdToNotice : notificationReceiverIds ) {
			final User userToNotice = userService.load( userIdToNotice );
			result.add( new UserNotification( userToNotice, getSendNotificationStrategy(), getNotificationData() ) );
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
