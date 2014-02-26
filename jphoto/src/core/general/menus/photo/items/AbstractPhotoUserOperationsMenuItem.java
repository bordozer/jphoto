package core.general.menus.photo.items;

import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;

public abstract class AbstractPhotoUserOperationsMenuItem extends AbstractPhotoMenuItem {

	public AbstractPhotoUserOperationsMenuItem( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	protected abstract boolean hasAccessTo();

	@Override
	public boolean isAccessibleFor() {

		if ( isAccessorSeeingMenuOfOwnPhoto() ) {
			return true;
		}

		if ( isAccessorSuperAdmin() ) {
			return false;
		}

		return hasAccessTo();
	}
}
