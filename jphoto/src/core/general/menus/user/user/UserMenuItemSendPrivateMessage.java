package core.general.menus.user.user;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.user.AbstractUserMenuItem;
import core.general.user.User;
import core.services.system.Services;

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
