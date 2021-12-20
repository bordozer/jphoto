package com.bordozer.jphoto.admin.controllers.jobs.edit.action;

import com.bordozer.jphoto.admin.controllers.jobs.edit.DateRangableModel;

public class PhotoActionGenerationModel extends DateRangableModel {

    public static final String TOTAL_ACTIONS_FORM_CONTROL = "totalActions";
    public static final String PHOTOS_QTY_FORM_CONTROL = "photosQty";

    private String totalActions;
    private String photosQty;

    public String getTotalActions() {
        return totalActions;
    }

    public void setTotalActions(final String totalActions) {
        this.totalActions = totalActions;
    }

    public String getPhotosQty() {
        return photosQty;
    }

    public void setPhotosQty(final String photosQty) {
        this.photosQty = photosQty;
    }

    @Override
    public void clear() {
        super.clear();
    }
}
