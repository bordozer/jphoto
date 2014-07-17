package ui.services.menu.entry.items.photo.admin;

import core.general.configuration.ConfigurationKey;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.photo.commands.PhotoMenuItemDeleteCommand;

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
	protected boolean isSystemConfigurationKeyIsOn() {
		return services.getConfigurationService().getBoolean( ConfigurationKey.ADMIN_CAN_DELETE_OTHER_PHOTOS );
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_ADMIN_CSS_CLASS;
	}
}