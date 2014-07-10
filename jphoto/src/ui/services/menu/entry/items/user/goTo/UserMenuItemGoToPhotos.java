package ui.services.menu.entry.items.user.goTo;

import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.user.AbstractUserMenuItem;

public class UserMenuItemGoToPhotos extends AbstractUserMenuItem {

	public UserMenuItemGoToPhotos( final User user, final User accessor, final Services services ) {
		super( user, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS;
	}

	@Override
	public AbstractEntryMenuItemCommand<User> getMenuItemCommand() {

		return new AbstractEntryMenuItemCommand<User>( menuEntry, accessor, services ) {

			@Override
			public String getMenuText() {
				final int photoQtyByUser = getPhotoQtyByUser( getId() );
				return getTranslatorService().translate( "PhotoMenuItem: $1: all photos ( $2 )", getLanguage(), menuEntry.getNameEscaped(), String.valueOf( photoQtyByUser ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotos( %d );", getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {

		if( getPhotoQtyByUser( getId() ) < 2 ) {
			return false;
		}

		return ! hideMenuItemBecauseEntryOfMenuCaller();
	}

	private int getPhotoQtyByUser( final int userId ) {
		return getPhotoService().getPhotoQtyByUser( userId );
	}
}
