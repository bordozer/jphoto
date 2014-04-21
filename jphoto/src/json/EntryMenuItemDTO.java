package json;

import core.general.menus.AbstractEntryMenuItem;

public class EntryMenuItemDTO {

	private int menuItemId;
	private final AbstractEntryMenuItem menuItem;

	public EntryMenuItemDTO( final int menuItemId, final AbstractEntryMenuItem menuItem ) {
		this.menuItemId = menuItemId;
		this.menuItem = menuItem;
	}

	public int getMenuItemId() {
		return menuItemId;
	}

	public AbstractEntryMenuItem getMenuItem() {
		return menuItem;
	}
}
