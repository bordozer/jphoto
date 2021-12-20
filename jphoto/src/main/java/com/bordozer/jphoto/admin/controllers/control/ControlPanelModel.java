package com.bordozer.jphoto.admin.controllers.control;

import com.bordozer.jphoto.core.general.base.AbstractGeneralPageModel;

public class ControlPanelModel extends AbstractGeneralPageModel {

    private int usersTotal;
    private int photosTotal;
    private int photoPreviewsTotal;
    private int photoCommentsTotal;
    private int privateMessagesTotal;

    public int getUsersTotal() {
        return usersTotal;
    }

    public void setUsersTotal(final int usersTotal) {
        this.usersTotal = usersTotal;
    }

    public int getPhotosTotal() {
        return photosTotal;
    }

    public void setPhotosTotal(final int photosTotal) {
        this.photosTotal = photosTotal;
    }

    public int getPhotoPreviewsTotal() {
        return photoPreviewsTotal;
    }

    public void setPhotoPreviewsTotal(final int photoPreviewsTotal) {
        this.photoPreviewsTotal = photoPreviewsTotal;
    }

    public int getPhotoCommentsTotal() {
        return photoCommentsTotal;
    }

    public void setPhotoCommentsTotal(final int photoCommentsTotal) {
        this.photoCommentsTotal = photoCommentsTotal;
    }

    public int getPrivateMessagesTotal() {
        return privateMessagesTotal;
    }

    public void setPrivateMessagesTotal(final int privateMessagesTotal) {
        this.privateMessagesTotal = privateMessagesTotal;
    }
}
