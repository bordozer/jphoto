package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.interfaces.Favoritable;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.user.UserService;

public class FavoritableFactory {

    public static Favoritable createEntry(final int favoriteEntryId, final FavoriteEntryType entryType, final UserService userService, final PhotoService photoService) {
        switch (entryType) {
            case FAVORITE_MEMBERS:
            case FRIENDS:
            case BLACKLIST:
            case NEW_PHOTO_NOTIFICATION:
            case MEMBERS_INVISIBILITY_LIST:
                return userService.load(favoriteEntryId);
            case FAVORITE_PHOTOS:
            case BOOKMARKED_PHOTOS:
            case NEW_COMMENTS_NOTIFICATION:
                return photoService.load(favoriteEntryId);
        }

        throw new IllegalArgumentException(String.format("Illegal FavoriteEntryType: %s", entryType));
    }
}
