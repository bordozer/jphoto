package com.bordozer.jphoto.core.services.notification;

import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.EmailNotificationType;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.utils.UserUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class UserNotificationsCollector {

    protected Services services;

    protected final LogHelper log = new LogHelper();

    public UserNotificationsCollector(final Services services) {
        this.services = services;
    }

    public abstract List<UserNotification> getUserNotifications();

    public abstract AbstractSendNotificationStrategy getSendNotificationStrategy();

    public abstract NotificationData getNotificationData();

    public static List<UserNotification> privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFavorites(final Photo photo, final Services services) {
        return privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy(photo, services).getUserNotifications();
    }

    public static List<UserNotification> privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFriends(final Photo photo, final Services services) {
        return privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFriendsStrategy(photo, services).getUserNotifications();
    }

    public static List<UserNotification> privateMessagesAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthor(final Photo photo, final Services services) {
        return privateMessagesAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthorStrategy(photo, services).getUserNotifications();
    }

    public static List<UserNotification> emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFavorites(final Photo photo, final Services services) {
        return emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy(photo, services).getUserNotifications();
    }

    public static List<UserNotification> emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFriends(final Photo photo, final Services services) {
        return emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFriendsStrategy(photo, services).getUserNotifications();
    }

    public static List<UserNotification> emailsAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthor(final Photo photo, final Services services) {
        return emailsAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthorStrategy(photo, services).getUserNotifications();
    }

    public static List<UserNotification> emailToPhotoAuthorAboutNewCommentToHisPhoto(final PhotoComment comment, final Services services) {
        return emailToPhotoAuthorAboutNewCommentToHisPhotoStrategy(comment, services).getUserNotifications();
    }

    public static List<UserNotification> emailsAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhoto(final PhotoComment comment, final Services services) {
        return emailsAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhotoStrategy(comment, services).getUserNotifications();
    }

    /* All users get Private message about event */
    private static UserNotificationsCollector privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy(final Photo photo, final Services services) {
        return new UserNotificationsCollector(services) {

            @Override
            public List<UserNotification> getUserNotifications() {
                return getNewPhotoUserNotificationForFavoriteType(FavoriteEntryType.FAVORITE_MEMBERS, photo.getUserId());
            }

            @Override
            public AbstractSendNotificationStrategy getSendNotificationStrategy() {
                return AbstractSendNotificationStrategy.SEND_PRIVATE_MESSAGE_STRATEGY;
            }

            @Override
            public NotificationData getNotificationData() {
                final User photoAuthor = getPhotoAuthor(photo);

                final TranslatableMessage subject = getSubject("UserNotificationsCollector: $1 has uploaded new photo '$2'", photoAuthor, photo, services);
                final TranslatableMessage message = getMessage("UserNotificationsCollector: One of your favorite members $1 has uploaded new photo '$2'.", photoAuthor, photo, services);

                return new NotificationData(subject, message);
            }
        };
    }

    /* All users get Private message about event */
    private static UserNotificationsCollector privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFriendsStrategy(final Photo photo, final Services services) {
        return new UserNotificationsCollector(services) {

            @Override
            public List<UserNotification> getUserNotifications() {
                return getNewPhotoUserNotificationForFavoriteType(FavoriteEntryType.FRIENDS, photo.getUserId());
            }

            @Override
            public AbstractSendNotificationStrategy getSendNotificationStrategy() {
                return AbstractSendNotificationStrategy.SEND_PRIVATE_MESSAGE_STRATEGY;
            }

            @Override
            public NotificationData getNotificationData() {
                final User photoAuthor = getPhotoAuthor(photo);

                final TranslatableMessage subject = getSubject("UserNotificationsCollector: $1 has uploaded new photo '$2'", photoAuthor, photo, services);
                final TranslatableMessage message = getMessage("UserNotificationsCollector: One of your friends $1 has uploaded new photo '$2'.", photoAuthor, photo, services);

                return new NotificationData(subject, message);
            }
        };
    }

    /* All users get Private message about event */
    private static UserNotificationsCollector privateMessagesAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthorStrategy(final Photo photo, final Services services) {
        return new UserNotificationsCollector(services) {

            @Override
            public List<UserNotification> getUserNotifications() {
                return getNewPhotoUserNotificationForFavoriteType(FavoriteEntryType.NEW_PHOTO_NOTIFICATION, photo.getUserId());
            }

            @Override
            public AbstractSendNotificationStrategy getSendNotificationStrategy() {
                return AbstractSendNotificationStrategy.SEND_PRIVATE_MESSAGE_STRATEGY;
            }

            @Override
            public NotificationData getNotificationData() {
                final User photoAuthor = getPhotoAuthor(photo);

                final TranslatableMessage subject = getSubject("UserNotificationsCollector: $1 has uploaded new photo '$2'", photoAuthor, photo, services);
                final TranslatableMessage message = getMessage("UserNotificationsCollector: $1 has uploaded new photo '$2'. You got this message because you are tracking new photos of $3.", photoAuthor, photo, services).string(photoAuthor.getNameEscaped());

                return new NotificationData(subject, message);
            }
        };
    }

    public static List<UserNotification> privateMessagesAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhoto(final PhotoComment comment, final Services services) {
        return privateMessagesAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhotoStrategy(comment, services).getUserNotifications();
    }

    /* Only users set corresponding option get Email about event */
    private static UserNotificationsCollector privateMessagesAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhotoStrategy(final PhotoComment comment, final Services services) {
        return new UserNotificationsCollector(services) {

            @Override
            public List<UserNotification> getUserNotifications() {
                final List<UserNotification> userNotifications = getNewPhotoUserNotificationForFavoriteType(FavoriteEntryType.NEW_COMMENTS_NOTIFICATION, comment.getPhotoId());

                removeNotificationsAboutOwnComments(userNotifications, comment);

                return userNotifications;
            }

            @Override
            public AbstractSendNotificationStrategy getSendNotificationStrategy() {
                return AbstractSendNotificationStrategy.SEND_PRIVATE_MESSAGE_STRATEGY;
            }

            @Override
            public NotificationData getNotificationData() {
                final User commentAuthor = comment.getCommentAuthor();
                final Photo photo = services.getPhotoService().load(comment.getPhotoId());

                final TranslatableMessage subject = getSubject("UserNotificationsCollector: $1 has commented photo '$2'", commentAuthor, photo, services);
                final TranslatableMessage message = getMessage("UserNotificationsCollector: $1 has commented photo '$2'. You got this message because you are tracking new comments to the photo.", commentAuthor, photo, services);

                return new NotificationData(subject, message);
            }
        };
    }

    /* Only users set corresponding option get Email about event */
    private static UserNotificationsCollector emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy(final Photo photo, final Services services) {
        return new UserNotificationsCollector(services) {

            @Override
            public List<UserNotification> getUserNotifications() {
                return removeThoseWhoDoNotWantToGetEmailAboutEvent(privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy(photo, services).getUserNotifications(), EmailNotificationType.NEW_PHOTO_OF_FAVORITE_MEMBER);
            }

            @Override
            public AbstractSendNotificationStrategy getSendNotificationStrategy() {
                return AbstractSendNotificationStrategy.SEND_EMAIL_STRATEGY;
            }

            @Override
            public NotificationData getNotificationData() {
                final User photoAuthor = getPhotoAuthor(photo);

                final TranslatableMessage subject = getSubject("UserNotificationsCollector: $1 has uploaded new photo '$2'", photoAuthor, photo, services);
                final TranslatableMessage message = getMessage1("UserNotificationsCollector: One of your favorite members $1 has uploaded new photo '$2'.", photoAuthor, photo, services);

                return new NotificationData(subject, message);
            }
        };
    }

    /* Only users set corresponding option get Email about event */
    private static UserNotificationsCollector emailsAboutNewPhotoToUsersWhoHavePhotoAuthorInFriendsStrategy(final Photo photo, final Services services) {
        return new UserNotificationsCollector(services) {

            @Override
            public List<UserNotification> getUserNotifications() {
                return removeThoseWhoDoNotWantToGetEmailAboutEvent(privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFriendsStrategy(photo, services).getUserNotifications(), EmailNotificationType.NEW_PHOTO_OF_FRIEND);
            }

            @Override
            public AbstractSendNotificationStrategy getSendNotificationStrategy() {
                return AbstractSendNotificationStrategy.SEND_EMAIL_STRATEGY;
            }

            @Override
            public NotificationData getNotificationData() {
                final User photoAuthor = getPhotoAuthor(photo);

                final TranslatableMessage subject = getSubject("UserNotificationsCollector: $1 has uploaded new photo '$2'", photoAuthor, photo, services);
                final TranslatableMessage message = getMessage1("UserNotificationsCollector: One of your friends $1 has uploaded new photo '$2'.", photoAuthor, photo, services);

                return new NotificationData(subject, message);
            }
        };
    }

    /* Only users set corresponding option get Email about event */
    private static UserNotificationsCollector emailsAboutNewPhotoToUsersWhoAreTrackingNewPhotosOfPhotoAuthorStrategy(final Photo photo, final Services services) {
        return new UserNotificationsCollector(services) {

            @Override
            public List<UserNotification> getUserNotifications() {
                return removeThoseWhoDoNotWantToGetEmailAboutEvent(privateMessagesAboutNewPhotoToUsersWhoHavePhotoAuthorInFavoritesStrategy(photo, services).getUserNotifications(), EmailNotificationType.NEW_PHOTO_OF_TRACKING_MEMBER);
            }

            @Override
            public AbstractSendNotificationStrategy getSendNotificationStrategy() {
                return AbstractSendNotificationStrategy.SEND_EMAIL_STRATEGY;
            }

            @Override
            public NotificationData getNotificationData() {
                final User photoAuthor = getPhotoAuthor(photo);

                final TranslatableMessage subject = getSubject("UserNotificationsCollector: $1 has uploaded new photo '$2'", photoAuthor, photo, services);
                final TranslatableMessage message = getMessage1("UserNotificationsCollector: $1 has uploaded new photo '$2'. You got this message because you are tracking new photos of $3.", photoAuthor, photo, services).string(photoAuthor.getNameEscaped());

                return new NotificationData(subject, message);
            }
        };
    }

    /* Only users set corresponding option get Email about event */
    private static UserNotificationsCollector emailToPhotoAuthorAboutNewCommentToHisPhotoStrategy(final PhotoComment comment, final Services services) {
        return new UserNotificationsCollector(services) {

            @Override
            public List<UserNotification> getUserNotifications() {
                final Photo photo = services.getPhotoService().load(comment.getPhotoId());
                final User photoAuthor = getPhotoAuthor(photo);

                if (isUserEmailNotificationOptionSwitchedOn(photoAuthor, EmailNotificationType.COMMENT_TO_USER_PHOTO)) {
                    return Collections.emptyList();
                }

                final boolean isPhotoAuthorCommentedHisOwnPhoto = UserUtils.isUsersEqual(photoAuthor, comment.getCommentAuthor());
                if (isPhotoAuthorCommentedHisOwnPhoto) {
                    return Collections.emptyList();
                }

                return newArrayList(new UserNotification(photoAuthor, getSendNotificationStrategy(), getNotificationData()));
            }

            @Override
            public AbstractSendNotificationStrategy getSendNotificationStrategy() {
                return AbstractSendNotificationStrategy.SEND_EMAIL_STRATEGY;
            }

            @Override
            public NotificationData getNotificationData() {
                final User commentAuthor = comment.getCommentAuthor();
                final Photo photo = services.getPhotoService().load(comment.getPhotoId());

                final TranslatableMessage subject = getSubject("UserNotificationsCollector: $1 has commented your photo '$2'", commentAuthor, photo, services);
                final TranslatableMessage message = getMessage1("UserNotificationsCollector: $1 has commented your photo '$2'", commentAuthor, photo, services);

                return new NotificationData(subject, message);
            }
        };
    }

    /* Only users set corresponding option get Email about event */
    private static UserNotificationsCollector emailsAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhotoStrategy(final PhotoComment comment, final Services services) {
        return new UserNotificationsCollector(services) {

            @Override
            public List<UserNotification> getUserNotifications() {
                final List<UserNotification> userNotifications = removeThoseWhoDoNotWantToGetEmailAboutEvent(privateMessagesAboutNewCommentToUsersWhoAreTrackingNewCommentsToCommentedPhotoStrategy(comment, services).getUserNotifications(), EmailNotificationType.COMMENT_TO_TRACKING_PHOTO);

                removeNotificationsAboutOwnComments(userNotifications, comment);

                return userNotifications;
            }

            @Override
            public AbstractSendNotificationStrategy getSendNotificationStrategy() {
                return AbstractSendNotificationStrategy.SEND_EMAIL_STRATEGY;
            }

            @Override
            public NotificationData getNotificationData() {
                final User commentAuthor = comment.getCommentAuthor();
                final Photo photo = services.getPhotoService().load(comment.getPhotoId());

                final TranslatableMessage subject = getSubject("UserNotificationsCollector: $1 has commented photo '$2'", commentAuthor, photo, services);
                final TranslatableMessage message = getMessage("UserNotificationsCollector: $1 has commented photo '$2'. You got this message because you are tracking new comments to the photo.", commentAuthor, photo, services);

                return new NotificationData(subject, message);
            }
        };
    }

    protected List<UserNotification> getNewPhotoUserNotificationForFavoriteType(final FavoriteEntryType favoriteEntryType, final int userId) {
        final UserService userService = services.getUserService();

        final List<UserNotification> result = newArrayList();

        final List<Integer> notificationReceiverIds = services.getFavoritesService().getAllUsersIdsWhoHasThisEntryInFavorites(userId, favoriteEntryType);
        for (final int userIdToNotice : notificationReceiverIds) {
            final User userToNotice = userService.load(userIdToNotice);
            result.add(new UserNotification(userToNotice, getSendNotificationStrategy(), getNotificationData()));
        }

        return result;
    }

    private static List<UserNotification> removeThoseWhoDoNotWantToGetEmailAboutEvent(final List<UserNotification> userNotifications, final EmailNotificationType emailNotificationType) {

        CollectionUtils.filter(userNotifications, new Predicate<UserNotification>() {
            @Override
            public boolean evaluate(final UserNotification userNotification) {
                return isUserEmailNotificationOptionSwitchedOn(userNotification.getUser(), emailNotificationType);
            }
        });

        return userNotifications;
    }

    protected void removeNotificationsAboutOwnComments(final List<UserNotification> userNotifications, final PhotoComment comment) {
        CollectionUtils.filter(userNotifications, new Predicate<UserNotification>() {
            @Override
            public boolean evaluate(final UserNotification userNotification) {
                return !UserUtils.isUsersEqual(userNotification.getUser(), comment.getCommentAuthor());
            }
        });
    }

    protected User getPhotoAuthor(final Photo photo) {
        return services.getUserService().load(photo.getUserId());
    }

    private static boolean isUserEmailNotificationOptionSwitchedOn(final User photoAuthor, final EmailNotificationType emailNotificationType) {
        return photoAuthor.getEmailNotificationTypes().contains(emailNotificationType);
    }

    private static TranslatableMessage getSubject(final String subj, final User photoAuthor, final Photo photo, final Services services) {
        return new TranslatableMessage(subj, services).string(photoAuthor.getNameEscaped()).string(photo.getNameEscaped());
    }

    private static TranslatableMessage getMessage(final String nerd, final User photoAuthor, final Photo photo, final Services services) {
        return new TranslatableMessage(nerd, services).userCardLink(photoAuthor).addPhotoCardLinkParameter(photo);
    }

    private static TranslatableMessage getMessage1(final String nerd, final User photoAuthor, final Photo photo, final Services services) {
        return getMessage(nerd, photoAuthor, photo, services).translatableString("UserNotificationsCollector: You can control email notifications in the settings of your profile.");
    }
}
