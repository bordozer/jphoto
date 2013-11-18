package core.services.system;

import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.services.entry.FavoritesService;
import core.services.entry.PrivateMessageService;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoService;
import core.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.collect.Lists.newArrayList;

public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private PhotoCommentService photoCommentService;

	@Autowired
	private FavoritesService favoritesService;

	@Autowired
	private PrivateMessageService privateMessageService;

	@Override
	public void newPhotoNotification( final Photo photo ) {
		/*final List<PrivateMessage> emailMessages = newArrayList();
		final List<PrivateMessage> privateMessages = newArrayList();

		addNotificationsAboutNewPhotoToUsersWhoHasAuthorInFavorites( photo, emailMessages, privateMessages );

		addNotificationsAboutNewPhotoToUsersWhoIsTrackingAuthorNewPhotos( photo, emailMessages );

		sendNotification( emailMessages );*/
	}

	@Override
	public void newCommentNotification( final PhotoComment comment ) {

		/*final List<PrivateMessage> emailMessages = newArrayList();

		addNotificationsToPhotoAuthorAboutNewCommentToHisPhoto( comment, emailMessages );

		addNotificationsToUsersWhoAreTrackingNewCommentsToThisPhoto( comment, emailMessages );

		sendNotification( emailMessages );*/
	}

/*	private void sendNotification( final List<PrivateMessage> emailMessages ) {
		for ( final PrivateMessage message : emailMessages ) {
			// TODO: send email
		}
	}

	private void addNotificationsToPhotoAuthorAboutNewCommentToHisPhoto( final PhotoComment comment, final List<PrivateMessage> emailMessages ) {

		final Photo photo = photoService.load( comment.getPhotoId() );

		if ( ! photo.isNotificationEmailAboutNewPhotoComment() ) {
			return;
		}

		final User photoAuthor = userService.load( photo.getUserId() );
		final User commentAuthor = userService.load( comment.getCommentAuthor().getId() );

		final boolean isPhotoAuthorCommentedHisOwnPhoto = UserUtils.isUsersEqual( photoAuthor, commentAuthor );
		if ( isPhotoAuthorCommentedHisOwnPhoto ) {
			return;
		}

		final PrivateMessage message = new PrivateMessage();
		message.setUser( photoAuthor );
		message.setToUser( photoAuthor );

		message.setAboutUser( commentAuthor );
		message.setAboutPhoto( photo );

		message.setPrivateMessageType( EmailNotificationType.COMMENT_TO_USER_PHOTO );
		message.setMessageText( comment.getCommentText() );

		if ( photo.isNotificationEmailAboutNewPhotoComment() ) {
			addToListIfEntryDoesNotExist( emailMessages, message );
		}
	}

	private void addNotificationsToUsersWhoAreTrackingNewCommentsToThisPhoto( final PhotoComment comment, final List<PrivateMessage> emailMessages ) {
		final Photo photo = photoService.load( comment.getPhotoId() );
		final User commentAuthor = userService.load( comment.getCommentAuthor().getId() );

		final List<Integer> subscribedOnCommentsUsers = favoritesService.getAllUsersIdsWhoHasThisEntryInFavorites( comment.getPhotoId(), FavoriteEntryType.NEW_COMMENTS_NOTIFICATION );
		for ( final int userId : subscribedOnCommentsUsers ) {
			final User user = userService.load( userId );

			boolean isCommentWrittenByUser = UserUtils.isUsersEqual( commentAuthor, user );
			if ( isCommentWrittenByUser ) {
				continue;
			}

			if ( ! isEmailNotification( user, EmailNotificationType.COMMENT_TO_TRACKING_PHOTO ) ) {
				continue;
			}

			final PrivateMessage message = new PrivateMessage();
			message.setUser( user );
			message.setToUser( user );

			message.setAboutUser( commentAuthor );
			message.setAboutPhoto( photo );

			message.setPrivateMessageType( EmailNotificationType.COMMENT_TO_TRACKING_PHOTO );
			message.setMessageText( comment.getCommentText() );

			if ( isEmailNotification( user, EmailNotificationType.COMMENT_TO_TRACKING_PHOTO ) ) {
				addToListIfEntryDoesNotExist( emailMessages, message );
			}
		}
	}

	private void addNotificationsAboutNewPhotoToUsersWhoHasAuthorInFavorites( final Photo photo, final List<PrivateMessage> emailMessages, final List<PrivateMessage> privateMessages ) {
		final FavoriteEntryType favoriteEntryType = FavoriteEntryType.USER;
		final EmailNotificationType emailNotificationType = EmailNotificationType.PHOTO_OF_FAVORITE_MEMBER;
		final EmailNotificationType privateMessageType = EmailNotificationType.PHOTO_OF_FAVORITE_MEMBER;

		addNotificationsAboutNewPhoto( photo, favoriteEntryType, emailNotificationType, emailMessages, privateMessageType );
	}

	private void addNotificationsAboutNewPhotoToUsersWhoIsTrackingAuthorNewPhotos( final Photo photo, final List<PrivateMessage> emailMessages ) {
		final FavoriteEntryType favoriteEntryType = FavoriteEntryType.NEW_PHOTO_NOTIFICATION;
		final EmailNotificationType emailNotificationType = EmailNotificationType.PHOTO_OF_TRACKING_MEMBER;
		final EmailNotificationType privateMessageType = EmailNotificationType.PHOTO_OF_TRACKING_MEMBER;

		addNotificationsAboutNewPhoto( photo, favoriteEntryType, emailNotificationType, emailMessages, privateMessageType );
	}

	private void addNotificationsAboutNewPhoto( final Photo photo, final FavoriteEntryType favoriteEntryType, final EmailNotificationType emailNotificationType, final List<PrivateMessage> emailMessages, final EmailNotificationType privateMessageType ) {
		final User photoAuthor = userService.load( photo.getUserId() );

		final List<Integer> usersWhoHasPhotoAuthorInFavorites = favoritesService.getAllUsersIdsWhoHasThisEntryInFavorites( photoAuthor.getId(), favoriteEntryType );
		for ( final int userId : usersWhoHasPhotoAuthorInFavorites ) {

			final User user = userService.load( userId );

			if ( ! isEmailNotification( user, emailNotificationType ) ) {
				continue;
			}

			final PrivateMessage message = new PrivateMessage();
			message.setUser( user );
			message.setToUser( user );

			message.setAboutUser( photoAuthor );
			message.setAboutPhoto( photo );

			message.setPrivateMessageType( privateMessageType );


			if ( isEmailNotification( user, emailNotificationType ) ) {
				addToListIfEntryDoesNotExist( emailMessages, message );
			}
		}
	}


	private boolean isEmailNotification( final User user, final EmailNotificationType emailNotificationType ) {
		return user.getEmailNotificationTypes().contains( emailNotificationType );

	}

	private <T> void addToListIfEntryDoesNotExist( final List<T> items, final T item ) {
		if ( ! items.contains( item ) ) {
			items.add( item );
		}
	}*/
}
