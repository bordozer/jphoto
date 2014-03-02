package core.services.entry;

import core.dtos.AjaxResultDTO;
import core.enums.FavoriteEntryType;
import core.general.favorite.FavoriteEntry;
import core.interfaces.BaseEntityService;

import java.util.Date;
import java.util.List;

public interface FavoritesService extends BaseEntityService<FavoriteEntry> {

	String BEAN_NAME = "favoritesService";

	boolean isEntryInFavorites( final int userWhoIsAddingToFavorites, final int beingAddedEntryId, final int entryTypeId );

	boolean isUserInBlackListOfUser( final int blackListOwnerId, final int beingCheckedUserId );

	FavoriteEntry getFavoriteEntry( final int userId, final int favoriteEntryId, final FavoriteEntryType entryType );

	boolean addEntryToFavorites( final int userId, final int favoriteEntryId, final Date time, final FavoriteEntryType entryType );

	AjaxResultDTO addEntryToFavoritesAjax( final int userId, final int photoId, final int entryTypeId );

	AjaxResultDTO removeEntryFromFavoritesAjax( final int userId, final int photoId, final int entryTypeId );

	List<Integer> getAllUsersIdsWhoHasThisEntryInFavorites( final int favoriteEntryId, final FavoriteEntryType favoriteEntryType );
}
