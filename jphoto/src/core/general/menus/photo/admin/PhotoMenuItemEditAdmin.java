package core.general.menus.photo.admin;

import core.general.configuration.ConfigurationKey;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.commands.PhotoMenuItemEditCommand;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;

public class PhotoMenuItemEditAdmin extends AbstractPhotoMenuItemOperationAdmin {

	public PhotoMenuItemEditAdmin( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_MENU_ITEM_EDIT;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new PhotoMenuItemEditCommand( menuEntry, services );
	}

	@Override
	protected boolean isOperationConfigurationOn() {
		return services.getConfigurationService().getBoolean( ConfigurationKey.ADMIN_CAN_EDIT_OTHER_PHOTOS );
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_CSS_CLASS_ADMIN;
	}
}
