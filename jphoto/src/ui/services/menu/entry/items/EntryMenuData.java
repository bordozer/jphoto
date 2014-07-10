package ui.services.menu.entry.items;

public class EntryMenuData {

	private final EntryMenuOperationType entryMenuItemType;

	private Object customObject;

	public EntryMenuData( final EntryMenuOperationType entryMenuItemType ) {
		this.entryMenuItemType = entryMenuItemType;
	}

	public EntryMenuOperationType getEntryMenuItemType() {
		return entryMenuItemType;
	}

	public int getId() {
		return entryMenuItemType.getId();
	}

	public String getIcon() {
		return entryMenuItemType.getIcon();
	}

	public boolean isSubMenu() {
		return entryMenuItemType.isSubMenu();
	}

	public Object getCustomObject() {
		return customObject;
	}

	public void setCustomObject( final Object customObject ) {
		this.customObject = customObject;
	}
}
