package core.services.notification;

import core.enums.FavoriteEntryType;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.EmailNotificationType;
import core.general.user.User;
import core.log.LogHelper;
import core.services.security.Services;
import core.services.translator.Language;
import core.services.user.UserService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import utils.UserUtils;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class UserNotificationsCollector {

	private static final String CONTROL_EMAIL_NOTIFICATIONS_HINT = "You can control email notifications in the settings of your profile.";

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
				final String photoCardLink = getPhotoCardLink( photo, Language.EN ); // TODO: pass addressat language here

				final String subject = String.format( "%s has uploaded new photo '%s'", photoAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "One of your favorite members %s has uploaded new photo '%s'."
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
				final String photoCardLink = getPhotoCardLink( photo, Language.EN ); // TODO: pass addressat language here

				final String subject = String.format( "%s has uploaded new photo '%s'", photoAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "One of your friends %s has uploaded new photo '%s'"
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
				final String photoCardLink = getPhotoCardLink( photo, Language.EN ); // TODO: pass addressat language here

				final String subject = String.format( "%s has uploaded new photo '%s'", photoAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "%s has uploaded new photo '%s'. You got this message because you are tracking new photos of %s."
					, userCardLink, photoCardLink, photoAuthor.getNameEscaped() );

				return new NotificationData( subject, message );
			}
		};
	}

	public static List<UserNotification> privateMessagesAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhoto( final PhotoComment comment, final Services services ) {
		return privateMessagesAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhotoStrategy( comment, services ).getUserNotifications();
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector privateMessagesAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhotoStrategy( final PhotoComment comment, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				final List<UserNotification> userNotifications = getNewPhotoUserNotificationForFavoriteType( FavoriteEntryType.NEW_COMMENTS_NOTIFICATION, comment.getPhotoId() );

				removeNotificationsAboutOwnComments( userNotifications, comment );

				return userNotifications;
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
				final String photoCardLink = getPhotoCardLink( photo, Language.EN ); // TODO: pass addressat language here

				final String subject = String.format( "%s has commented photo '%s'.", commentAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "%s has commented photo '%s'. You got this message because you are tracking new comments to the photo."
					, commentAuthorCardLink, photoCardLink );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return removeThoseWhoDoNotWantToGetEmailAboutEvent( privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy( photo, services ).getUserNotifications(), EmailNotificationType.NEW_PHOTO_OF_FAVORITE_MEMBER );
			}

			@Override
			public AbstractSendNotificationStrategy getSendNotificationStrategy() {
				return AbstractSendNotificationStrategy.SEND_EMAIL_STRATEGY;
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String userCardLink = getUserCardLink( photoAuthor );
				final String photoCardLink = getPhotoCardLink( photo, Language.EN ); // TODO: pass addressat language here

				final String subject = String.format( "%s has uploaded new photo '%s'", photoAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "One of your favorite member %s has uploaded new photo '%s'. %s"
					, userCardLink, photoCardLink, CONTROL_EMAIL_NOTIFICATIONS_HINT );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFriendsStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return removeThoseWhoDoNotWantToGetEmailAboutEvent( privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFriendsStrategy( photo, services ).getUserNotifications(), EmailNotificationType.NEW_PHOTO_OF_FRIEND );
			}

			@Override
			public AbstractSendNotificationStrategy getSendNotificationStrategy() {
				return AbstractSendNotificationStrategy.SEND_EMAIL_STRATEGY;
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String userCardLink = getUserCardLink( photoAuthor );
				final String photoCardLink = getPhotoCardLink( photo, Language.EN ); // TODO: pass addressat language here

				final String subject = String.format( "%s has uploaded new photo '%s'", photoAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "One of your friend %s has uploaded new photo '%s'. %s", userCardLink, photoCardLink, CONTROL_EMAIL_NOTIFICATIONS_HINT );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector emailsAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthorStrategy( final Photo photo, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				return removeThoseWhoDoNotWantToGetEmailAboutEvent( privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy( photo, services ).getUserNotifications(), EmailNotificationType.NEW_PHOTO_OF_TRACKING_MEMBER );
			}

			@Override
			public AbstractSendNotificationStrategy getSendNotificationStrategy() {
				return AbstractSendNotificationStrategy.SEND_EMAIL_STRATEGY;
			}

			@Override
			public NotificationData getNotificationData() {
				final User photoAuthor = getPhotoAuthor( photo );

				final String userCardLink = getUserCardLink( photoAuthor );
				final String photoCardLink = getPhotoCardLink( photo, Language.EN ); // TODO: pass addressat language here

				final String subject = String.format( "%s has uploaded new photo '%s'", photoAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "%s has uploaded new photo '%s'. You got this message because you are tracking new photos of photo author. %s"
					, userCardLink, photoCardLink, CONTROL_EMAIL_NOTIFICATIONS_HINT );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector emailToPhotoAuthorAboutNewCommentToHisPhotoStrategy( final PhotoComment comment, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				final Photo photo = services.getPhotoService().load( comment.getPhotoId() );
				final User photoAuthor = getPhotoAuthor( photo );

				if ( isUserEmailNotificationOptionSwitchedOn( photoAuthor, EmailNotificationType.COMMENT_TO_USER_PHOTO ) ) {
					return Collections.emptyList();
				}

				final boolean isPhotoAuthorCommentedHisOwnPhoto = UserUtils.isUsersEqual( photoAuthor, comment.getCommentAuthor() );
				if ( isPhotoAuthorCommentedHisOwnPhoto ) {
					return Collections.emptyList();
				}

				return newArrayList( new UserNotification( photoAuthor, getSendNotificationStrategy(), getNotificationData() ) );
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
				final String photoCardLink = getPhotoCardLink( photo, Language.EN ); // TODO: pass addressat language here

				final String subject = String.format( "%s has commented your photo '%s'", commentAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "%s has commented your photo '%s'. %s", commentAuthorCardLink, photoCardLink, CONTROL_EMAIL_NOTIFICATIONS_HINT );

				return new NotificationData( subject, message );
			}
		};
	}

	/* Only users set corresponding option get Email about event */
	private static UserNotificationsCollector emailsAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhotoStrategy( final PhotoComment comment, final Services services ) {
		return new UserNotificationsCollector( services ) {

			@Override
			public List<UserNotification> getUserNotifications() {
				final List<UserNotification> userNotifications = removeThoseWhoDoNotWantToGetEmailAboutEvent( privateMessagesAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhotoStrategy( comment, services ).getUserNotifications(), EmailNotificationType.COMMENT_TO_TRACKING_PHOTO );

				removeNotificationsAboutOwnComments( userNotifications, comment );

				return userNotifications;
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
				final String photoCardLink = getPhotoCardLink( photo, Language.EN ); // TODO: pass addressat language here

				final String subject = String.format( "%s has commented photo '%s'", commentAuthor.getNameEscaped(), photo.getNameEscaped() );
				final String message = String.format( "%s has commented photo '%s'. You got this email because you are tracking new comments to the photo."
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

	private static List<UserNotification> removeThoseWhoDoNotWantToGetEmailAboutEvent( final List<UserNotification> userNotifications, final EmailNotificationType emailNotificationType ) {

		CollectionUtils.filter( userNotifications, new Predicate<UserNotification>() {
			@Override
			public boolean evaluate( final UserNotification userNotification ) {
				return isUserEmailNotificationOptionSwitchedOn( userNotification.getUser(), emailNotificationType );
			}
		} );

		return userNotifications;
	}

	protected void removeNotificationsAboutOwnComments( final List<UserNotification> userNotifications, final PhotoComment comment ) {
		CollectionUtils.filter( userNotifications, new Predicate<UserNotification>() {
			@Override
			public boolean evaluate( final UserNotification userNotification ) {
				return !UserUtils.isUsersEqual( userNotification.getUser(), comment.getCommentAuthor() );
			}
		} );
	}

	protected User getPhotoAuthor( final Photo photo ) {
		return services.getUserService().load( photo.getUserId() );
	}

	protected String getPhotoCardLink( final Photo photo, final Language language ) {
		return services.getEntityLinkUtilsService().getPhotoCardLink( photo, language );
	}

	protected String getUserCardLink( final User photoAuthor ) {
		return services.getEntityLinkUtilsService().getUserCardLink( photoAuthor, Language.EN ); // TODO: pass addressat language here
	}

	private static boolean isUserEmailNotificationOptionSwitchedOn( final User photoAuthor, final EmailNotificationType emailNotificationType ) {
		return photoAuthor.getEmailNotificationTypes().contains( emailNotificationType );
	}
}
