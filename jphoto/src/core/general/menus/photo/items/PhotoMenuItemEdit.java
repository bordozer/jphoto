package core.general.menus.photo.items;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;

public class PhotoMenuItemEdit extends AbstractPhotoUserOperationsMenuItem {

	public PhotoMenuItemEdit( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.MENU_ITEM_EDIT;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Edit photo" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "editPhotoData( %d );", getId() );
			}
		};
	}

	@Override
	protected boolean hasAccessTo() {
		return getSecurityService().userCanEditPhoto( accessor, menuEntry );
	}
}
