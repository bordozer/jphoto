package core.general.menus.photo.goTo;

import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;

public abstract class AbstractGoToAuthorPhotos extends AbstractPhotoMenuItem {

	protected abstract int getPhotosQty();

	public AbstractGoToAuthorPhotos( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public boolean isAccessibleFor() {

		if ( getPhotosQty() < 2 ) {
			return false;
		}

		if ( isSuperAdminUser( accessor ) ) {
			return true;
		}

		if ( hideMenuItemBecauseEntryOfMenuCaller() ) {
			return false;
		}

		if ( isPhotoIsWithinAnonymousPeriod() ) {
			return false;
		}

		return true;
	}
}
