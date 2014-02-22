package core.general.menus.user;

import core.general.user.User;
import core.general.menus.AbstractEntryMenuItem;
import core.services.entry.FavoritesService;
import core.services.security.Services;
import utils.UserUtils;

public abstract class AbstractUserMenuItem extends AbstractEntryMenuItem<User> {

	protected AbstractUserMenuItem( final User user, final User accessor, final Services services ) {
		super( user, accessor, services );
	}

	@Override
	public abstract boolean isAccessibleFor( final User user, final User userWhoIsCallingMenu );
	
	protected boolean isMenuCallerIsSeeingOwnMenu( final User user, final User userWhoIsCallingMenu ) {
		return UserUtils.isUsersEqual( userWhoIsCallingMenu, user );
	}

	protected boolean hideMenuItemBecauseEntryOfMenuCaller( final User user, final User userWhoIsCallingMenu ) {
		return isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff() && isMenuCallerIsSeeingOwnMenu( user, userWhoIsCallingMenu );
	}

}
