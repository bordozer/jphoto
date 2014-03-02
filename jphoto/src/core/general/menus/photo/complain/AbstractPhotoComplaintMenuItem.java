package core.general.menus.photo.complain;

import core.general.menus.comment.ComplaintReasonType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;

public abstract class AbstractPhotoComplaintMenuItem extends AbstractPhotoMenuItem {

	protected abstract ComplaintReasonType getComplainReasonType();

	protected abstract String getMenuItemText();

	public AbstractPhotoComplaintMenuItem( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public boolean isAccessibleFor() {

		if ( ! isMenuAccessorLogged() ) {
			return false;
		}

		if ( isPhotoOfMenuAccessor() ) {
			return false;
		}

		return true;
	}
}
