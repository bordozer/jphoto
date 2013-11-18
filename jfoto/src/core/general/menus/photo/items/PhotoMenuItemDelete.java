package core.general.menus.photo.items;

import core.general.photo.Photo;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import utils.TranslatorUtils;

public class PhotoMenuItemDelete extends AbstractPhotoMenuItem {

	public static final String BEAN_NAME = "photoMenuItemDelete";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.MENU_ITEM_DELETE;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int photoId, final User userWhoIsCallingMenu ) {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Delete photo" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "deletePhoto( %d ); return false;", photoId );
			}
		};
	}

	@Override
	public boolean isAccessibleForPhoto( final Photo photo, final User userWhoIsCallingMenu ) {
		return securityService.userCanDeletePhoto( userWhoIsCallingMenu, photo );
	}
}
