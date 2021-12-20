package com.bordozer.jphoto.core.general.user;

import com.bordozer.jphoto.core.interfaces.IdentifiableNameable;

public enum EmailNotificationType implements IdentifiableNameable {

    NEW_PHOTO_OF_FAVORITE_MEMBER(1, "EmailNotificationType: New photo of one of your favorite members"), NEW_PHOTO_OF_FRIEND(8, "EmailNotificationType: New photo of your friend"), NEW_PHOTO_OF_TRACKING_MEMBER(4, "EmailNotificationType: New photo of member who's new photos you are tracking"), COMMENT_TO_USER_PHOTO(2, "EmailNotificationType: New comment to your photo"), COMMENT_TO_TRACKING_PHOTO(3, "EmailNotificationType: New comment to photo which comments you are tracking"), PRIVATE_MESSAGE(5, "EmailNotificationType: New private message"), ADMIN_MESSAGE(6, "EmailNotificationType: Messages from com.bordozer.jphoto.admin"), SYSTEM_INFORMATION(7, "EmailNotificationType: Messages from system");

    private final int id;
    private final String name;

    private EmailNotificationType(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static EmailNotificationType getById(final int id) {
        for (final EmailNotificationType entryType : EmailNotificationType.values()) {
            if (entryType.getId() == id) {
                return entryType;
            }
        }

        return null;
    }
}
