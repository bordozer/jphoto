package core.services.entry;

import core.enums.FavoriteEntryType;
import core.general.favorite.FavoriteEntry;
import core.general.user.User;
import core.interfaces.BaseEntityService;

import java.util.Date;
import java.util.List;

public interface FavoritesService extends BaseEntityService<FavoriteEntry> {

	String BEAN_NAME = "favoritesService";

	boolean isEntryInFavorites( final int userWhoIsAddingToFavorites, final int beingAddedEntryId, final int entryTypeId );

	boolean isUserInBlackListOfUser( final int blackListOwnerId, final int beingCheckedUserId );

	boolean isUserInMembersInvosobolityListOfUser( final int userId, final int beingCheckedUserId );

	FavoriteEntry getFavoriteEntry( final int userId, final int favoriteEntryId, final FavoriteEntryType entryType );

	boolean addEntryToFavorites( final int userId, final int favoriteEntryId, final Date time, final FavoriteEntryType entryType );

	boolean removeEntryFromFavorites( final int userId, final int favoriteEntryId, final FavoriteEntryType byId );

	List<Integer> getAllUsersIdsWhoHasThisEntryInFavorites( final int favoriteEntryId, final FavoriteEntryType favoriteEntryType );

	int getUsersQtyWhoNewPhotoUserIsTracking( final User user );

	int getPhotoQtyWhichCommentsUserIsTracking( final User user );

	int getFriendsQty( final User user );
}
