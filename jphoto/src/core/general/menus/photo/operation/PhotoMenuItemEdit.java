package core.general.menus.photo.operation;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.commands.PhotoMenuItemEditCommand;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;

public class PhotoMenuItemEdit extends AbstractPhotoUserOperationsMenuItem {

	public PhotoMenuItemEdit( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.MENU_ITEM_EDIT;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new PhotoMenuItemEditCommand( menuEntry );
	}

	@Override
	public boolean hasAccessTo() {
		return getSecurityService().userCanEditPhoto( accessor, menuEntry );
	}

}
