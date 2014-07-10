package ui.services.menu.entry.items.photo.complain;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.comment.ComplaintReasonType;
import ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

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
