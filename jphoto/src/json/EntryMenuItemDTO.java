package json;

import core.general.menus.AbstractEntryMenuItem;

public class EntryMenuItemDTO {

	private String menuItemId;
	private final AbstractEntryMenuItem menuItem;

	public EntryMenuItemDTO( final String menuItemId, final AbstractEntryMenuItem menuItem ) {
		this.menuItemId = menuItemId;
		this.menuItem = menuItem;
	}

	public String getMenuItemId() {
		return menuItemId;
	}

	public AbstractEntryMenuItem getMenuItem() {
		return menuItem;
	}
}
