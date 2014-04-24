package json;

import java.util.List;

public class EntryMenuItemDTO {

	private String menuItemId;
	private boolean menuTypeSeparator;
	private String menuCssClass;

	private String menuCommand;
	private String menuCommandIcon;
	private String menuCommandText;

	private boolean hasSumMenu;
	private List<EntryMenuItemDTO> entrySubMenuItemDTOs;

	public EntryMenuItemDTO( final String menuItemId ) {
		this.menuItemId = menuItemId;
	}

	public String getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId( final String menuItemId ) {
		this.menuItemId = menuItemId;
	}

	public String getMenuCssClass() {
		return menuCssClass;
	}

	public void setMenuCssClass( final String menuCssClass ) {
		this.menuCssClass = menuCssClass;
	}

	public boolean isMenuTypeSeparator() {
		return menuTypeSeparator;
	}

	public void setMenuTypeSeparator( final boolean menuTypeSeparator ) {
		this.menuTypeSeparator = menuTypeSeparator;
	}

	public String getMenuCommand() {
		return menuCommand;
	}

	public void setMenuCommand( final String menuCommand ) {
		this.menuCommand = menuCommand;
	}

	public String getMenuCommandIcon() {
		return menuCommandIcon;
	}

	public void setMenuCommandIcon( final String menuCommandIcon ) {
		this.menuCommandIcon = menuCommandIcon;
	}

	public String getMenuCommandText() {
		return menuCommandText;
	}

	public void setMenuCommandText( final String menuCommandText ) {
		this.menuCommandText = menuCommandText;
	}

	public boolean isHasSumMenu() {
		return hasSumMenu;
	}

	public void setHasSumMenu( final boolean hasSumMenu ) {
		this.hasSumMenu = hasSumMenu;
	}

	public List<EntryMenuItemDTO> getEntrySubMenuItemDTOs() {
		return entrySubMenuItemDTOs;
	}

	public void setEntrySubMenuItemDTOs( final List<EntryMenuItemDTO> entrySubMenuItemDTOs ) {
		this.entrySubMenuItemDTOs = entrySubMenuItemDTOs;
	}

	@Override
	public String toString() {

		if ( menuTypeSeparator ) {
			return "SEPARATOR";
		}

		if ( hasSumMenu ) {
			String.format( "%s: submenu ( %d items )", menuCommandText, entrySubMenuItemDTOs.size() );
		}

		return String.format( "%s: %s", menuCommandText, menuCommand );
	}
}
