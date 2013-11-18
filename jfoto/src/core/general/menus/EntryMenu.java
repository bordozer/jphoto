package core.general.menus;

import java.util.List;

public class EntryMenu {

	private final int entryId;
	private final EntryMenuType entryMenuType;

	private final List<? extends AbstractEntryMenuItem> entryMenuItems;

	public EntryMenu( final int entryId, final EntryMenuType entryMenuType, final List<? extends AbstractEntryMenuItem> entryMenuItems ) {
		this.entryId = entryId;
		this.entryMenuType = entryMenuType;
		this.entryMenuItems = entryMenuItems;
	}

	public int getEntryId() {
		return entryId;
	}

	public String getMenuId() {
		return String.format( "menu_%d_%d", entryMenuType.getId(), entryId );
	}

	public List<? extends AbstractEntryMenuItem> getEntryMenuItems() {
		return entryMenuItems;
	}

	public EntryMenuType getEntryMenuType() {
		return entryMenuType;
	}
}
