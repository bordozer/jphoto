package core.services.user;

import core.services.dao.FavoritesDao;
import core.services.dao.PhotoCommentDao;
import org.springframework.beans.factory.annotation.Autowired;

public class UserStatisticServiceImpl implements UserStatisticService {

	@Autowired
	private FavoritesDao favoritesDao;

	@Autowired
	private PhotoCommentDao photoCommentDao;

	@Override
	public int getFavoritePhotosQty( final int userId ) {
		return favoritesDao.getFavoritePhotosQty( userId );
	}

	@Override
	public int getBookmarkedPhotosQty( final int userId ) {
		return favoritesDao.getBookmarkedPhotosQty( userId );
	}

	@Override
	public int getUsersQtyWhoAddedInFavoriteMembers( final int userId ) {
		return favoritesDao.getUsersQtyWhoAddedInFavoriteMembers( userId );
	}

	@Override
	public int getWrittenCommentsQty( final int userId ) {
		return photoCommentDao.getWrittenCommentsQty( userId );
	}

	@Override
	public int getReceivedCommentsQty( final int userId ) {
		return photoCommentDao.getReceivedCommentsQty( userId );
	}

	@Override
	public int getFriendsQty( final int userId ) {
		return favoritesDao.getFriendsQty( userId );
	}

	@Override
	public int getFavoriteMembersQty( final int userId ) {
		return favoritesDao.getFavoriteMembersQty( userId );
	}

	@Override
	public int getBackListEntriesQty( final int userId ) {
		return favoritesDao.getBackListEntriesQty( userId );
	}

	@Override
	public int getNotificationsAboutNewPhotosQty( final int userId ) {
		return favoritesDao.getNotificationsAboutNewPhotosQty( userId );
	}

	@Override
	public int getNotificationsAboutNewCommentsQty( final int userId ) {
		return favoritesDao.getNotificationsAboutNewCommentsQty( userId );
	}

	@Override
	public int setReceivedUnreadCommentsQty( final int userId ) {
		return photoCommentDao.getUnreadCommentsQty( userId );
	}
}
