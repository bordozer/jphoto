package core.general.menus;

public abstract class AbstractEntryMenuItemCommand<T extends PopupMenuAssignable> {

	protected final T menuEntry;
	protected final EntryMenuOperationType entryMenuType;

	public AbstractEntryMenuItemCommand( final T menuEntry, final EntryMenuOperationType entryMenuType ) {
		this.menuEntry = menuEntry;
		this.entryMenuType = entryMenuType;
	}

	public abstract String getMenuText();

	public abstract String getMenuCommand();

	public String getCommandIcon() {
		return String.format( "menus/%s", entryMenuType.getIcon() );
	}

	public int getId() {
		return menuEntry.getId();
	}
}
