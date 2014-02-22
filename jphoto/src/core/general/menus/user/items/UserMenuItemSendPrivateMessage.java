package core.general.menus.user.items;

import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.user.AbstractUserMenuItem;
import core.services.security.Services;
import utils.TranslatorUtils;
import utils.UserUtils;

public class UserMenuItemSendPrivateMessage extends AbstractUserMenuItem {

	public UserMenuItemSendPrivateMessage( final User user, final User accessor, final Services services ) {
		super( user, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.SEND_PRIVATE_MESSAGE;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Send private message to $1", menuEntry.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "sendPrivateMessage( %d, %d, '%s' );", accessor.getId(), getId(), menuEntry.getNameEscaped() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		if ( ! UserUtils.isUsersEqual( accessor, menuEntry ) && isSuperAdminUser( accessor ) ) {
			return true;
		}

		return isUserWhoIsCallingMenuLogged( accessor )
			   && ! isMenuCallerIsSeeingOwnMenu()
			   && ! getFavoritesService().isUserInBlackListOfUser( getId(), accessor.getId() );
	}
}
