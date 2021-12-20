package com.bordozer.jphoto.ui.controllers.photos.activity;

import com.bordozer.jphoto.core.general.base.AbstractGeneralPageModel;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.ui.activity.AbstractActivityStreamEntry;

import java.util.List;

public class PhotoActivityStreamModel extends AbstractGeneralPageModel {

    private final Photo photo;
    private List<AbstractActivityStreamEntry> activities;
    private int filterActivityTypeId;

    public PhotoActivityStreamModel(final Photo photo) {
        this.photo = photo;
    }

    public Photo getPhoto() {
        return photo;
    }

    public List<AbstractActivityStreamEntry> getActivities() {
        return activities;
    }

    public void setActivities(final List<AbstractActivityStreamEntry> activities) {
        this.activities = activities;
    }

    public int getFilterActivityTypeId() {
        return filterActivityTypeId;
    }

    public void setFilterActivityTypeId(final int filterActivityTypeId) {
        this.filterActivityTypeId = filterActivityTypeId;
    }
}
