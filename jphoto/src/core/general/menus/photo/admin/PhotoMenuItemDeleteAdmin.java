package core.general.menus.photo.admin;

import core.general.configuration.ConfigurationKey;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;

public class PhotoMenuItemDeleteAdmin extends AbstractPhotoMenuItem {

	public PhotoMenuItemDeleteAdmin( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_MENU_ITEM_DELETE;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {
			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Delete photo" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "deletePhoto( %d ); return false;", getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return isAccessorSuperAdmin() && services.getConfigurationService().getBoolean( ConfigurationKey.ADMIN_CAN_DELETE_OTHER_PHOTOS );
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_CSS_CLASS_ADMIN;
	}
}
