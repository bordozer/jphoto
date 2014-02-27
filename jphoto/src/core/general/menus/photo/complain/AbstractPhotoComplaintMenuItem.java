package core.general.menus.photo.complain;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.comment.ComplaintReasonType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;

public abstract class AbstractPhotoComplaintMenuItem extends AbstractPhotoMenuItem {

	protected abstract ComplaintReasonType getComplainReasonType();

	protected abstract String getMenuItemText();

	public AbstractPhotoComplaintMenuItem( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public boolean isAccessibleFor() {

		if ( ! isUserWhoIsCallingMenuLogged() ) {
			return false;
		}

		if ( isPhotoOfMenuCaller() ) {
			return false;
		}

		return true;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<Photo>( menuEntry ) {
			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Complaint copyright" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "complaintPhotoCopyright( %d );", getId() );
			}
		};
	}
}
