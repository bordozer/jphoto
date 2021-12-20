package com.bordozer.jphoto.core.services.user;

public interface UserStatisticService {

    int getFavoritePhotosQty(final int userId);

    int getBookmarkedPhotosQty(final int userId);

    int getUsersQtyWhoAddedInFavoriteMembers(final int userId);

    int getWrittenCommentsQty(final int userId);

    int getReceivedCommentsQty(final int userId);

    int getFriendsQty(final int userId);

    int getFavoriteMembersQty(final int userId);

    int getPhotosQtyOfUserFavoriteMembers(final int userId);

    int getBackListEntriesQty(final int userId);

    int getNotificationsAboutNewPhotosQty(final int userId);

    int getNotificationsAboutNewCommentsQty(final int userId);

    int setReceivedUnreadCommentsQty(final int userId);
}
