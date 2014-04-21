package json;

import java.util.List;

public class EntryMenuDTO {

	private final int entryId;
	private int entryMenuTypeId;

	private String menuDivId;
	private String menuId;

	private String entryMenuTypeName;
	private String entryMenuTitle;
	private String entryMenuHeight;

	private String menuItemCssClass;
	private String menuItemCommand;
	private String menuItemCommandText;
	private List<EntryMenuItemDTO> entryMenuItemDTOs;

	public EntryMenuDTO( final int entryMenuTypeId, final int entryId ) {
		this.entryId = entryId;
		this.entryMenuTypeId = entryMenuTypeId;
	}

	public int getEntryId() {
		return entryId;
	}

	public int getEntryMenuTypeId() {
		return entryMenuTypeId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId( final String menuId ) {
		this.menuId = menuId;
	}

	public String getMenuDivId() {
		return menuDivId;
	}

	public void setMenuDivId( final String menuDivId ) {
		this.menuDivId = menuDivId;
	}

	public String getEntryMenuTypeName() {
		return entryMenuTypeName;
	}

	public void setEntryMenuTypeName( final String entryMenuTypeName ) {
		this.entryMenuTypeName = entryMenuTypeName;
	}

	public String getEntryMenuTitle() {
		return entryMenuTitle;
	}

	public String getEntryMenuHeight() {
		return entryMenuHeight;
	}

	public void setEntryMenuHeight( final String entryMenuHeight ) {
		this.entryMenuHeight = entryMenuHeight;
	}

	public void setEntryMenuTitle( final String entryMenuTitle ) {
		this.entryMenuTitle = entryMenuTitle;
	}

	public String getMenuItemCssClass() {
		return menuItemCssClass;
	}

	public void setMenuItemCssClass( final String menuItemCssClass ) {
		this.menuItemCssClass = menuItemCssClass;
	}

	public String getMenuItemCommand() {
		return menuItemCommand;
	}

	public void setMenuItemCommand( final String menuItemCommand ) {
		this.menuItemCommand = menuItemCommand;
	}

	public String getMenuItemCommandText() {
		return menuItemCommandText;
	}

	public void setMenuItemCommandText( final String menuItemCommandText ) {
		this.menuItemCommandText = menuItemCommandText;
	}

	public void setEntryMenuItemDTOs( final List<EntryMenuItemDTO> entryMenuItemDTOs ) {
		this.entryMenuItemDTOs = entryMenuItemDTOs;
	}

	public List<EntryMenuItemDTO> getEntryMenuItemDTOs() {
		return entryMenuItemDTOs;
	}
}
