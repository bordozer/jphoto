package ui.services.menu.entry.items.user;

import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItem;
import utils.UserUtils;

public abstract class AbstractUserMenuItem extends AbstractEntryMenuItem<User> {

	protected AbstractUserMenuItem( final User user, final User accessor, final Services services ) {
		super( user, accessor, services );
	}

	protected boolean isUserCallingHisOwnMenu() {
		return UserUtils.isUsersEqual( accessor, menuEntry );
	}

	protected boolean hideMenuItemBecauseEntryOfMenuCaller() {
		return isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff() && isUserCallingHisOwnMenu();
	}

	protected boolean isAccessorSuperAdmin() {
		return getSecurityService().isSuperAdminUser( accessor.getId() );
	}

	protected boolean isUserSuperAdmin() {
		return getSecurityService().isSuperAdminUser( menuEntry );
	}
}
