package com.bordozer.jphoto.utils;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.ui.context.EnvironmentContext;

public class UserUtils {

    public static boolean isUsersEqual(final int user1Id, final int user2Id) {
        return user1Id == user2Id;
    }

    public static boolean isUsersEqual(final User user1, final User user2) {
        return isUsersEqual(user1.getId(), user2.getId());
    }

    public static boolean isLoggedUser(final int userId) {
        return userId > 0;
    }

    public static boolean isLoggedUser(final User user) {
        return isLoggedUser(user.getId());
    }

    public static boolean isCurrentUserLoggedUser() {
        return isLoggedUser(EnvironmentContext.getCurrentUser());
    }

    public static boolean isTheUserThatWhoIsCurrentUser(final User user) {
        return isTheUserThatWhoIsCurrentUser(user.getId());
    }

    public static boolean isTheUserThatWhoIsCurrentUser(final int userId) {
        return isLoggedUser(userId) && isUsersEqual(EnvironmentContext.getCurrentUser().getId(), userId);
    }

    public static boolean isUserOwnThePhoto(final User user, final Photo photo) {
        return photo.getUserId() == user.getId();
    }
}
