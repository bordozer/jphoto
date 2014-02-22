package core.general.menus.photo.items;

import core.general.photo.Photo;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.services.security.Services;
import utils.TranslatorUtils;

public class PhotoMenuItemEdit extends AbstractPhotoMenuItem {

	public PhotoMenuItemEdit( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.MENU_ITEM_EDIT;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {

		final int photoId = menuEntry.getId();

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( services.getSecurityService().userOwnThePhoto( accessor, photoId ) ? "Edit your photo" : "Edit photo (ADMIN)" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "editPhotoData( %d );", photoId );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return services.getSecurityService().userCanEditPhoto( accessor, menuEntry );
	}
}
