package com.bordozer.jphoto.rest.editableList.albums;

import com.bordozer.jphoto.rest.editableList.AbstractEditableListEntryDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAlbumDTO extends AbstractEditableListEntryDTO {

    private String albumLink;
    private int albumPhotosQty;

    public UserAlbumDTO() {
        super();
    }

    public UserAlbumDTO(final int entryId) {
        super(entryId);
    }

    public void setAlbumLink(final String albumLink) {
        this.albumLink = albumLink;
    }

    public String getAlbumLink() {
        return albumLink;
    }

    public void setAlbumPhotosQty(final int albumPhotosQty) {
        this.albumPhotosQty = albumPhotosQty;
    }

    public int getAlbumPhotosQty() {
        return albumPhotosQty;
    }

    @Override
    public String toString() {
        return String.format("User: %d, album: %d '%s'", userId, entryId, entryName);
    }
}
