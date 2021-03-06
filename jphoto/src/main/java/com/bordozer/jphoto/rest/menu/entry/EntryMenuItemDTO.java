package com.bordozer.jphoto.rest.menu.entry;

import java.util.List;

public class EntryMenuItemDTO {

    private boolean menuTypeSeparator;

    private String menuCssUniqueClass;
    private String menuCssClassBG;

    private String callbackMessage;

    private String menuCommand;
    private String menuCommandIcon;
    private String menuCommandText;

    private boolean hasSumMenu;
    private List<EntryMenuItemDTO> entrySubMenuItemDTOs;

    public String getMenuCssUniqueClass() {
        return menuCssUniqueClass;
    }

    public void setMenuCssUniqueClass(final String menuCssUniqueClass) {
        this.menuCssUniqueClass = menuCssUniqueClass;
    }

    public String getMenuCssClassBG() {
        return menuCssClassBG;
    }

    public void setMenuCssClassBG(final String menuCssClassBG) {
        this.menuCssClassBG = menuCssClassBG;
    }

    public void setCallbackMessage(final String callbackMessage) {
        this.callbackMessage = callbackMessage;
    }

    public String getCallbackMessage() {
        return callbackMessage;
    }

    public boolean isMenuTypeSeparator() {
        return menuTypeSeparator;
    }

    public void setMenuTypeSeparator(final boolean menuTypeSeparator) {
        this.menuTypeSeparator = menuTypeSeparator;
    }

    public String getMenuCommand() {
        return menuCommand;
    }

    public void setMenuCommand(final String menuCommand) {
        this.menuCommand = menuCommand;
    }

    public String getMenuCommandIcon() {
        return menuCommandIcon;
    }

    public void setMenuCommandIcon(final String menuCommandIcon) {
        this.menuCommandIcon = menuCommandIcon;
    }

    public String getMenuCommandText() {
        return menuCommandText;
    }

    public void setMenuCommandText(final String menuCommandText) {
        this.menuCommandText = menuCommandText;
    }

    public boolean isHasSumMenu() {
        return hasSumMenu;
    }

    public void setHasSumMenu(final boolean hasSumMenu) {
        this.hasSumMenu = hasSumMenu;
    }

    public List<EntryMenuItemDTO> getEntrySubMenuItemDTOs() {
        return entrySubMenuItemDTOs;
    }

    public void setEntrySubMenuItemDTOs(final List<EntryMenuItemDTO> entrySubMenuItemDTOs) {
        this.entrySubMenuItemDTOs = entrySubMenuItemDTOs;
    }

    @Override
    public String toString() {

        if (menuTypeSeparator) {
            return "SEPARATOR";
        }

        if (hasSumMenu) {
            String.format("%s: submenu ( %d items )", menuCommandText, entrySubMenuItemDTOs.size());
        }

        return String.format("%s: %s", menuCommandText, menuCommand);
    }
}
