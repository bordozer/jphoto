package ui.services.menu.entry.items.photo.admin;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

public abstract class AbstractPhotoMenuItemOperationAdmin extends AbstractPhotoMenuItem {

	public AbstractPhotoMenuItemOperationAdmin( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	protected abstract boolean isSystemConfigurationKeyIsOn();

	@Override
	public boolean isAccessibleFor() {

		if ( isAccessorSeeingMenuOfOwnPhoto() ) {
			return false;
		}

		return isAccessorSuperAdmin() && isSystemConfigurationKeyIsOn();
	}
}
