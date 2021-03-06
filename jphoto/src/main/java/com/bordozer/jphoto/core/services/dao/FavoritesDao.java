package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.general.favorite.FavoriteEntry;

import java.util.Date;
import java.util.List;

public interface FavoritesDao extends BaseEntityDao<FavoriteEntry> {

    FavoriteEntry getFavoriteEntry(final int userId, final int favoriteEntryId, final FavoriteEntryType entryType);

    boolean addEntryToFavorites(final int userId, final int favoriteEntryId, final Date time, final FavoriteEntryType entryType);

    boolean removeEntryFromFavorites(final int userId, final int favoriteEntryId, final FavoriteEntryType entryType);

    int getFavoritePhotosQty(final int userId);

    int getBookmarkedPhotosQty(final int userId);

    int getUsersQtyWhoAddedInFavoriteMembers(final int userId);

    int getFriendsQty(final int userId);

    int getFavoriteMembersQty(final int userId);

    int getBackListEntriesQty(final int userId);

    int getMembersInvisibilityListEntriesQty(final int userId);

    int getNotificationsAboutNewPhotosQty(final int userId);

    int getNotificationsAboutNewCommentsQty(final int userId);

    List<Integer> getAllUsersIdsWhoHasThisEntryInFavorites(final int favoriteEntryId, final FavoriteEntryType favoriteEntryType);

    int getPhotosOfUserFavoriteMembersQty(final int userId);
}
