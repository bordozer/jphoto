package menuItems.photo;

import core.general.user.User;

class GoToParameters {

	private final User accessor;
	private final int genrePhotosQty;
	private boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn;
	private boolean photoAuthorNameMustBeHidden;

	public GoToParameters( final User accessor, final int genrePhotosQty ) {
		this.accessor = accessor;
		this.genrePhotosQty = genrePhotosQty;
	}

	public User getAccessor() {
		return accessor;
	}

	public int getGenrePhotosQty() {
		return genrePhotosQty;
	}

	public boolean isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn() {
		return showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn;
	}

	public void setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) {
		this.showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn;
	}

	public boolean isPhotoAuthorNameMustBeHidden() {
		return photoAuthorNameMustBeHidden;
	}

	public void setPhotoAuthorNameMustBeHidden( final boolean photoAuthorNameMustBeHidden ) {
		this.photoAuthorNameMustBeHidden = photoAuthorNameMustBeHidden;
	}
}
