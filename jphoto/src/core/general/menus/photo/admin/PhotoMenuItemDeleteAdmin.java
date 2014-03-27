package core.general.menus.photo.admin;

import core.general.configuration.ConfigurationKey;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.commands.PhotoMenuItemDeleteCommand;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;

public class PhotoMenuItemDeleteAdmin extends AbstractPhotoMenuItemOperationAdmin {

	public PhotoMenuItemDeleteAdmin( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_MENU_ITEM_DELETE;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new PhotoMenuItemDeleteCommand( menuEntry, accessor, services );
	}

	@Override
	protected boolean isOperationConfigurationOn() {
		return services.getConfigurationService().getBoolean( ConfigurationKey.ADMIN_CAN_DELETE_OTHER_PHOTOS );
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_CSS_CLASS_ADMIN;
	}
}
