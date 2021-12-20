package com.bordozer.jphoto.rest.portal.photos;

import com.bordozer.jphoto.core.general.img.Dimension;

public class LatestPhotoDTO {

    private int photoId;
    private String photoName;
    private String photoImageUrl;
    private String photoCardUrl;

    private Dimension dimension;
    private int index;

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(final int photoId) {
        this.photoId = photoId;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(final String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoImageUrl() {
        return photoImageUrl;
    }

    public void setPhotoImageUrl(final String photoImageUrl) {
        this.photoImageUrl = photoImageUrl;
    }

    public String getPhotoCardUrl() {
        return photoCardUrl;
    }

    public void setPhotoCardUrl(final String photoCardUrl) {
        this.photoCardUrl = photoCardUrl;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(final Dimension dimension) {
        this.dimension = dimension;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return String.format("#%d: %s", photoId, photoName);
    }
}
