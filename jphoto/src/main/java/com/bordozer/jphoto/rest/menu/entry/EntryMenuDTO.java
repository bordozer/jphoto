package com.bordozer.jphoto.rest.menu.entry;

import java.util.List;

public class EntryMenuDTO {

    private final int entryId;
    private int entryMenuTypeId;

    private String entryMenuTypeName;
    private String entryMenuTitle;
    private String entryMenuHeight;

    private List<EntryMenuItemDTO> entryMenuItemDTOs;

    public EntryMenuDTO(final int entryMenuTypeId, final int entryId) {
        this.entryId = entryId;
        this.entryMenuTypeId = entryMenuTypeId;
    }

    public int getEntryId() {
        return entryId;
    }

    public int getEntryMenuTypeId() {
        return entryMenuTypeId;
    }

    public String getEntryMenuTypeName() {
        return entryMenuTypeName;
    }

    public void setEntryMenuTypeName(final String entryMenuTypeName) {
        this.entryMenuTypeName = entryMenuTypeName;
    }

    public String getEntryMenuTitle() {
        return entryMenuTitle;
    }

    public String getEntryMenuHeight() {
        return entryMenuHeight;
    }

    public void setEntryMenuHeight(final String entryMenuHeight) {
        this.entryMenuHeight = entryMenuHeight;
    }

    public void setEntryMenuTitle(final String entryMenuTitle) {
        this.entryMenuTitle = entryMenuTitle;
    }

    public void setEntryMenuItemDTOs(final List<EntryMenuItemDTO> entryMenuItemDTOs) {
        this.entryMenuItemDTOs = entryMenuItemDTOs;
    }

    public List<EntryMenuItemDTO> getEntryMenuItemDTOs() {
        return entryMenuItemDTOs;
    }
}
