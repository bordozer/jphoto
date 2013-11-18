package core.services.dao;

import core.enums.FavoriteEntryType;
import core.interfaces.Favoritable;
import core.services.photo.PhotoService;
import core.services.user.UserService;

public class FavoritableFactory {

	public static Favoritable createEntry( final int favoriteEntryId, final FavoriteEntryType entryType, final UserService userService, final PhotoService photoService ) {
		switch ( entryType ) {
			case USER:
			case FRIEND:
			case BLACKLIST:
			case NEW_PHOTO_NOTIFICATION:
				return userService.load( favoriteEntryId );
			case PHOTO:
			case BOOKMARK:
			case NEW_COMMENTS_NOTIFICATION:
				return photoService.load( favoriteEntryId );
		}

		throw new IllegalArgumentException( String.format( "Illegal FavoriteEntryType: %s", entryType ) );
	}
}
