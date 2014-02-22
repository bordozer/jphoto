package core.general.menus;

import java.util.List;

public class EntryMenu {

	private final PopupMenuAssignable menuEntry;
	private final EntryMenuType entryMenuType;

	private final List<? extends AbstractEntryMenuItem> entryMenuItems;

	public EntryMenu( final PopupMenuAssignable menuEntry, final EntryMenuType entryMenuType, final List<? extends AbstractEntryMenuItem> entryMenuItems ) {
		this.menuEntry = menuEntry;
		this.entryMenuType = entryMenuType;
		this.entryMenuItems = entryMenuItems;
	}

	public int getEntryId() {
		return menuEntry.getId();
	}

	public String getMenuId() {
		return String.format( "menu_%d_%d", entryMenuType.getId(), getEntryId() );
	}

	public List<? extends AbstractEntryMenuItem> getEntryMenuItems() {
		return entryMenuItems;
	}

	public EntryMenuType getEntryMenuType() {
		return entryMenuType;
	}

	public int getMenuHeight() {
		int result = 0;

		for ( final AbstractEntryMenuItem entryMenuItem : entryMenuItems ) {
			result += entryMenuItem.getHeight();
		}

		return result;
	}
}
