package core.general.menus.photo.admin;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.commands.PhotoMenuItemEditCommand;
import core.general.menus.photo.operation.AbstractPhotoUserOperationsMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;

public class PhotoMenuItemEditAdmin extends AbstractPhotoUserOperationsMenuItem {

	public PhotoMenuItemEditAdmin( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_MENU_ITEM_EDIT;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new PhotoMenuItemEditCommand( menuEntry, getEntryMenuType() );
	}

	@Override
	public boolean hasAccessTo() {
		return false;
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_CSS_CLASS_ADMIN;
	}
}
