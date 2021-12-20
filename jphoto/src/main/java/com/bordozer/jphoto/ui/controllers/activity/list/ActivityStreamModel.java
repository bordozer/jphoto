package com.bordozer.jphoto.ui.controllers.activity.list;

import com.bordozer.jphoto.core.general.base.AbstractGeneralPageModel;
import com.bordozer.jphoto.ui.activity.AbstractActivityStreamEntry;

import java.util.List;

public class ActivityStreamModel extends AbstractGeneralPageModel {

    private List<AbstractActivityStreamEntry> activities;
    private int filterActivityTypeId;

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
