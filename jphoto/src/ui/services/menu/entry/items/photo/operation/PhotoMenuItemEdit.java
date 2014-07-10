package ui.services.menu.entry.items.photo.operation;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.photo.commands.PhotoMenuItemEditCommand;

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
		return new PhotoMenuItemEditCommand( menuEntry, accessor, services );
	}

	@Override
	public boolean hasAccessTo() {
		return getSecurityService().userCanEditPhoto( accessor, menuEntry );
	}

}
