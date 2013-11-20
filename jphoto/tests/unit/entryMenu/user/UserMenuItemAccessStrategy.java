package entryMenu.user;

import core.general.menus.user.AbstractUserMenuItem;
import entryMenu.AbstractEntryMenuItemAccessStrategy;
import entryMenu.Initialable;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

abstract class UserMenuItemAccessStrategy extends AbstractEntryMenuItemAccessStrategy<AbstractUserMenuItem> {

	public static UserMenuItemAccessStrategy getUserMenuItemInaccessibleStrategy() {

		return new UserMenuItemAccessStrategy() {
			@Override
			public void assertMenuItemAccess( final AbstractUserMenuItem menuItem, final Initialable initialConditions ) {
				final UserInitialConditions conditions = ( UserInitialConditions ) initialConditions;
				assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, menuItem.isAccessibleForUser( conditions.getUser(), conditions.getUserWhoIsCallingMenu() ) );
			}
		};
	}

	public static UserMenuItemAccessStrategy getUserMenuItemAccessibleStrategy() {

		return new UserMenuItemAccessStrategy() {
			@Override
			public void assertMenuItemAccess( final AbstractUserMenuItem menuItem, final Initialable initialConditions ) {
				final UserInitialConditions conditions = ( UserInitialConditions ) initialConditions;
				assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, menuItem.isAccessibleForUser( conditions.getUser(), conditions.getUserWhoIsCallingMenu() ) );
			}
		};
	}
}
