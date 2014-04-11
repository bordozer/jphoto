package core.services.entry;

import core.enums.FavoriteEntryType;
import core.general.configuration.ConfigurationKey;
import core.general.favorite.FavoriteEntry;
import core.general.user.User;
import core.services.dao.FavoritesDao;
import core.services.system.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class FavoritesServiceImpl implements FavoritesService {

	@Autowired
	private FavoritesDao favoritesDao;

	@Autowired
	private ActivityStreamService activityStreamService;

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public boolean isEntryInFavorites( final int userId, final int beingAddedEntryId, final int entryTypeId ) {
		return getFavoriteEntry( userId, beingAddedEntryId, FavoriteEntryType.getById( entryTypeId ) ) != null;
	}

	@Override
	public boolean isUserInBlackListOfUser( final int blackListOwnerId, final int beingCheckedUserId ) {
		return isEntryInFavorites( blackListOwnerId, beingCheckedUserId, FavoriteEntryType.BLACKLIST.getId() );
	}

	@Override
	public FavoriteEntry getFavoriteEntry( final int userId, final int favoriteEntryId, final FavoriteEntryType entryType ) {
		return favoritesDao.getFavoriteEntry( userId, favoriteEntryId, entryType );
	}

	@Override
	public boolean addEntryToFavorites( final int userId, final int favoriteEntryId, final Date time, final FavoriteEntryType entryType ) {
		final boolean isAdded = favoritesDao.addEntryToFavorites( userId, favoriteEntryId, time, entryType );

		if ( isAdded && configurationService.getBoolean( ConfigurationKey.SYSTEM_ACTIVITY_LOG_FAVORITE_ACTIONS ) ) {
			activityStreamService.saveFavoriteAction( userId, favoriteEntryId, time, entryType );
		}

		return isAdded;
	}

	@Override
	public boolean removeEntryFromFavorites( final int userId, final int favoriteEntryId, final FavoriteEntryType byId ) {
		return favoritesDao.removeEntryFromFavorites( userId, favoriteEntryId, byId );
	}

	@Override
	public List<Integer> getAllUsersIdsWhoHasThisEntryInFavorites( final int favoriteEntryId, final FavoriteEntryType favoriteEntryType ) {
		return favoritesDao.getAllUsersIdsWhoHasThisEntryInFavorites( favoriteEntryId, favoriteEntryType );
	}

	@Override
	public int getUsersQtyWhoNewPhotoUserIsTracking( final User user ) {
		return favoritesDao.getNotificationsAboutNewPhotosQty( user.getId() );
	}

	@Override
	public int getPhotoQtyWhichCommentsUserIsTracking( final User user ) {
		return favoritesDao.getNotificationsAboutNewCommentsQty( user.getId() );
	}

	@Override
	public int getFriendsQty( final User user ) {
		return favoritesDao.getFriendsQty( user.getId() );
	}

	@Override
	public boolean save( final FavoriteEntry entry ) {
		return favoritesDao.saveToDB( entry );
	}

	@Override
	public FavoriteEntry load( final int id ) {
		return favoritesDao.load( id );
	}

	@Override
	public boolean delete( final int entryId ) {
		return favoritesDao.delete( entryId );
	}

	@Override
	public boolean exists( final int entryId ) {
		return favoritesDao.exists( entryId );
	}

	@Override
	public boolean exists( final FavoriteEntry entry ) {
		return favoritesDao.exists( entry );
	}
}
