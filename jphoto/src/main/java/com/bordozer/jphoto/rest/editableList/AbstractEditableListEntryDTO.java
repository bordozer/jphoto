package com.bordozer.jphoto.rest.editableList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractEditableListEntryDTO {

    protected int entryId;
    protected int userId;
    protected String entryName;

    public AbstractEditableListEntryDTO() {
    }

    public AbstractEditableListEntryDTO(int entryId) {
        this.entryId = entryId;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public void setUserId(final int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }
}
