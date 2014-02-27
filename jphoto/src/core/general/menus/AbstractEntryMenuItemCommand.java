package core.general.menus;

public abstract class AbstractEntryMenuItemCommand<T extends PopupMenuAssignable> {

	protected final T menuEntry;

	public AbstractEntryMenuItemCommand( final T menuEntry ) {
		this.menuEntry = menuEntry;
	}

	public abstract String getMenuText();

	public abstract String getMenuCommand();

	public int getId() {
		return menuEntry.getId();
	}
}
