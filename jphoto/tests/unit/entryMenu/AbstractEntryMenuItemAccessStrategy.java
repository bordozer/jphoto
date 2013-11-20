package entryMenu;

import core.general.menus.AbstractEntryMenuItem;

public abstract class AbstractEntryMenuItemAccessStrategy<T extends AbstractEntryMenuItem> {

	public static final String MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS = "Menu item should not be accessible but it is";
	public static final String MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT = "Menu item should be accessible but it is not";

	public abstract <E extends Initialable> void assertMenuItemAccess( final T menuItem, final E initialConditions );
}
