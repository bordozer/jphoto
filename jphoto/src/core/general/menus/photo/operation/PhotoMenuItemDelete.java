package core.general.menus.photo.operation;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.commands.PhotoMenuItemDeleteCommand;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;

public class PhotoMenuItemDelete extends AbstractPhotoUserOperationsMenuItem {

	public PhotoMenuItemDelete( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.MENU_ITEM_DELETE;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new PhotoMenuItemDeleteCommand( menuEntry, getEntryMenuType() );
	}

	@Override
	public boolean hasAccessTo() {
		return getSecurityService().userCanDeletePhoto( accessor, menuEntry );
	}
}
