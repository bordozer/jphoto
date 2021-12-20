package com.bordozer.jphoto.ui.viewModes;

public class PhotoListViewMode {

    private final PhotoListViewModeType viewModeType;
    private final String viewModeLink;

    public PhotoListViewMode(final PhotoListViewModeType viewModeType, final String viewModeLink) {
        this.viewModeType = viewModeType;
        this.viewModeLink = viewModeLink;
    }

    public PhotoListViewModeType getViewModeType() {
        return viewModeType;
    }

    public String getViewModeLink() {
        return viewModeLink;
    }

    public static PhotoListViewMode preview(final String viewModeLink) {
        return new PhotoListViewMode(PhotoListViewModeType.VIEW_MODE_PREVIEW, viewModeLink);
    }

    public static PhotoListViewMode details(final String viewModeLink) {
        return new PhotoListViewMode(PhotoListViewModeType.VIEW_MODE_BIG_PREVIEW, viewModeLink);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", viewModeType, viewModeLink);
    }
}
