package core.general.menus.photo.complain;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.AbstractEntryMenuItemComplaintCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.EntryMenuType;
import core.general.menus.comment.ComplaintReasonType;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;

public class PhotoComplaintCopyrightMenuItem extends AbstractPhotoComplaintMenuItem {

	public PhotoComplaintCopyrightMenuItem( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.PHOTO_COMPLAINT_COPYRIGHT;
	}

	@Override
	protected String getMenuItemText() {
		return "Complaint copyright";
	}

	@Override
	protected ComplaintReasonType getComplainReasonType() {
		return ComplaintReasonType.PHOTO_COPYRIGHT_COMPLAINT;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new AbstractEntryMenuItemComplaintCommand<Photo>( menuEntry, accessor, EntryMenuType.PHOTO, getComplainReasonType(), services ) {
			@Override
			public String getMenuText() {
				return getTranslatorService().translate( getMenuItemText(), getLanguage() );
			}
		};
	}
}
