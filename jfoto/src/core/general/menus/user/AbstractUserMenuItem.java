package core.general.menus.user;

import core.general.user.User;
import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import utils.UserUtils;

public abstract class AbstractUserMenuItem extends AbstractEntryMenuItem {

	protected abstract AbstractEntryMenuItemCommand initMenuItemCommand( final int userId, final User userWhoIsCallingMenu );

	public abstract boolean isAccessibleForUser( final User user, final User userWhoIsCallingMenu );
	
	protected boolean isMenuCallerIsSeeingOwnMenu( final User user, final User userWhoIsCallingMenu ) {
		return UserUtils.isUsersEqual( userWhoIsCallingMenu, user );
	}

	protected boolean hideMenuItemBecauseEntryOfMenuCaller( final User user, final User userWhoIsCallingMenu ) {
		return isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff() && isMenuCallerIsSeeingOwnMenu( user, userWhoIsCallingMenu );
	}
}
