package core.general.menus.user.items;

import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.user.AbstractUserMenuItem;
import utils.TranslatorUtils;

public class UserMenuItemGoToPhotos extends AbstractUserMenuItem {

	public static final String BEAN_NAME = "userMenuItemGoToPhotos";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int userId, final User userWhoIsCallingMenu ) {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				final User user = userService.load( userId );
				final int photoQtyByUser = getPhotoQtyByUser( userId );
				return TranslatorUtils.translate( "$1: all photos ( $2 )", user.getNameEscaped(), String.valueOf( photoQtyByUser ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotos( %d );", userId );
			}
		};
	}

	@Override
	public boolean isAccessibleForUser( final User user, final User userWhoIsCallingMenu ) {
		return ! hideMenuItemBecauseEntryOfMenuCaller( user, userWhoIsCallingMenu) && getPhotoQtyByUser( user.getId() ) > 0;
	}

	private int getPhotoQtyByUser( final int userId ) {
		return photoService.getPhotoQtyByUser( userId );
	}
}
