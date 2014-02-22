package core.general.menus.user.items;

import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.user.AbstractUserMenuItem;
import core.services.security.Services;
import utils.TranslatorUtils;

public class UserMenuItemGoToPhotos extends AbstractUserMenuItem {

	public UserMenuItemGoToPhotos( final User user, final User accessor, final Services services ) {
		super( user, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				final int photoQtyByUser = getPhotoQtyByUser( getId() );
				return TranslatorUtils.translate( "$1: all photos ( $2 )", menuEntry.getNameEscaped(), String.valueOf( photoQtyByUser ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotos( %d );", getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return ! hideMenuItemBecauseEntryOfMenuCaller() && getPhotoQtyByUser( getId() ) > 0;
	}

	private int getPhotoQtyByUser( final int userId ) {
		return getPhotoService().getPhotoQtyByUser( userId );
	}
}
