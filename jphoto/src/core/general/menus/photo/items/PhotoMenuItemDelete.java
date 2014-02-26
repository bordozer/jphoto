package core.general.menus.photo.items;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;

public class PhotoMenuItemDelete extends AbstractPhotoUserOperationsMenuItem {

	public PhotoMenuItemDelete( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.MENU_ITEM_DELETE;
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
	protected boolean hasAccessTo() {
		return getSecurityService().userCanDeletePhoto( accessor, menuEntry );
	}
}
