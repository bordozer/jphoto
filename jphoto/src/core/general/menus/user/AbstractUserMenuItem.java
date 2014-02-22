package core.general.menus.user;

import core.general.menus.AbstractEntryMenuItem;
import core.general.user.User;
import core.services.security.Services;
import utils.UserUtils;

public abstract class AbstractUserMenuItem extends AbstractEntryMenuItem<User> {

	protected AbstractUserMenuItem( final User user, final User accessor, final Services services ) {
		super( user, accessor, services );
	}

	protected boolean isMenuCallerIsSeeingOwnMenu() {
		return UserUtils.isUsersEqual( accessor, menuEntry );
	}

	protected boolean hideMenuItemBecauseEntryOfMenuCaller() {
		return isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff() && isMenuCallerIsSeeingOwnMenu();
	}

}
