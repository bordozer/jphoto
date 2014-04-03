package ui.controllers.users.card;

public class UserStatistic {

	private int favoritePhotosQty;
	private int bookmarkedPhotosQty;
	private int usersQtyWhoAddedInFavoriteMembers;

	private int writtenCommentsQty;
	private int receivedCommentsQty;
	private int receivedUnreadCommentsQty;

	private int friendsQty;
	private int favoriteMembersQty;
	private int photosOfFavoriteMembersQty;
	private int blackListEntriesQty;

	private int notificationsAboutNewPhotosQty;
	private int notificationsAboutNewCommentsQty;

	public int getReceivedCommentsQty() {
		return receivedCommentsQty;
	}

	public void setReceivedCommentsQty( final int receivedCommentsQty ) {
		this.receivedCommentsQty = receivedCommentsQty;
	}

	public int getReceivedUnreadCommentsQty() {
		return receivedUnreadCommentsQty;
	}

	public void setReceivedUnreadCommentsQty( final int receivedUnreadCommentsQty ) {
		this.receivedUnreadCommentsQty = receivedUnreadCommentsQty;
	}

	public int getWrittenCommentsQty() {
		return writtenCommentsQty;
	}

	public void setWrittenCommentsQty( final int writtenCommentsQty ) {
		this.writtenCommentsQty = writtenCommentsQty;
	}

	public int getFavoritePhotosQty() {
		return favoritePhotosQty;
	}

	public void setFavoritePhotosQty( final int favoritePhotosQty ) {
		this.favoritePhotosQty = favoritePhotosQty;
	}

	public int getBookmarkedPhotosQty() {
		return bookmarkedPhotosQty;
	}

	public void setBookmarkedPhotosQty( final int bookmarkedPhotosQty ) {
		this.bookmarkedPhotosQty = bookmarkedPhotosQty;
	}

	public int getUsersQtyWhoAddedInFavoriteMembers() {
		return usersQtyWhoAddedInFavoriteMembers;
	}

	public void setUsersQtyWhoAddedInFavoriteMembers( final int usersQtyWhoAddedInFavoriteMembers ) {
		this.usersQtyWhoAddedInFavoriteMembers = usersQtyWhoAddedInFavoriteMembers;
	}

	public int getFriendsQty() {
		return friendsQty;
	}

	public void setFriendsQty( final int friendsQty ) {
		this.friendsQty = friendsQty;
	}

	public int getFavoriteMembersQty() {
		return favoriteMembersQty;
	}

	public void setFavoriteMembersQty( final int favoriteMembersQty ) {
		this.favoriteMembersQty = favoriteMembersQty;
	}

	public int getPhotosOfFavoriteMembersQty() {
		return photosOfFavoriteMembersQty;
	}

	public void setPhotosOfFavoriteMembersQty( final int photosOfFavoriteMembersQty ) {
		this.photosOfFavoriteMembersQty = photosOfFavoriteMembersQty;
	}

	public int getBlackListEntriesQty() {
		return blackListEntriesQty;
	}

	public void setBlackListEntriesQty( final int blackListEntriesQty ) {
		this.blackListEntriesQty = blackListEntriesQty;
	}

	public int getNotificationsAboutNewPhotosQty() {
		return notificationsAboutNewPhotosQty;
	}

	public void setNotificationsAboutNewPhotosQty( final int notificationsAboutNewPhotosQty ) {
		this.notificationsAboutNewPhotosQty = notificationsAboutNewPhotosQty;
	}

	public int getNotificationsAboutNewCommentsQty() {
		return notificationsAboutNewCommentsQty;
	}

	public void setNotificationsAboutNewCommentsQty( final int notificationsAboutNewCommentsQty ) {
		this.notificationsAboutNewCommentsQty = notificationsAboutNewCommentsQty;
	}
}
