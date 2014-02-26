package menuItems.photo.goTo;

import core.general.user.User;

class GoToParameters {

	private final User accessor;
	private int photosQty;

	public GoToParameters( final User accessor ) {
		this.accessor = accessor;
	}

	GoToParameters( final User accessor, final int photosQty ) {
		this.accessor = accessor;
		this.photosQty = photosQty;
	}

	public User getAccessor() {
		return accessor;
	}

	public int getPhotosQty() {
		return photosQty;
	}

	public void setPhotosQty( final int photosQty ) {
		this.photosQty = photosQty;
	}
}
