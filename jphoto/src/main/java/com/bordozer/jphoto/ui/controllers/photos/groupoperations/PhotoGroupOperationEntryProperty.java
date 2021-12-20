package com.bordozer.jphoto.ui.controllers.photos.groupoperations;

public class PhotoGroupOperationEntryProperty {

    private final int photoId;
    private final int entryId;
    private final String name;
    private boolean value;

    public PhotoGroupOperationEntryProperty(final int photoId, final int entryId, final String name) {
        this.photoId = photoId;
        this.entryId = entryId;
        this.name = name;
    }

    public int getPhotoId() {
        return photoId;
    }

    public int getEntryId() {
        return entryId;
    }

    public String getName() {
        return name;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(final boolean value) {
        this.value = value;
    }
}
