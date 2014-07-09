package core.general.menus.photo.admin;

import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;

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
