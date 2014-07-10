package ui.services.menu.entry.items.user.user;

import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.user.AbstractUserMenuItem;

public class UserMenuItemSendPrivateMessage extends AbstractUserMenuItem {

	public UserMenuItemSendPrivateMessage( final User user, final User accessor, final Services services ) {
		super( user, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.SEND_PRIVATE_MESSAGE;
	}

	@Override
	public AbstractEntryMenuItemCommand<User> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<User>( menuEntry, accessor, services ) {

			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "Send private message to $1", getLanguage(), menuEntry.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "sendPrivateMessage( %d, %d, '%s' );", accessor.getId(), getId(), menuEntry.getNameEscaped() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		if ( ! isMenuAccessorLogged() ) {
			return false;
		}

		if ( isUserCallingHisOwnMenu() ) {
			return false;
		}

		if ( isSuperAdminUser( accessor ) ) {
			return true;
		}

		return !getFavoritesService().isUserInBlackListOfUser( getId(), accessor.getId() );
	}
}
