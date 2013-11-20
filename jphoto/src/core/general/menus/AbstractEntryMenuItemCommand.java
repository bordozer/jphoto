package core.general.menus;

public abstract class AbstractEntryMenuItemCommand {

	protected final EntryMenuOperationType entryMenuType;

	public AbstractEntryMenuItemCommand( final EntryMenuOperationType entryMenuType ) {
		this.entryMenuType = entryMenuType;
	}

	public abstract String getMenuText();

	public abstract String getMenuCommand();

	public String getCommandIcon() {
		return String.format( "menus/%s", entryMenuType.getIcon() );
	}
}
