package ui.services.menu.entry.items.photo.operation;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

public abstract class AbstractPhotoUserOperationsMenuItem extends AbstractPhotoMenuItem {

	public AbstractPhotoUserOperationsMenuItem( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	public abstract boolean hasAccessTo();

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
