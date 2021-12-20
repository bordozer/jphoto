package com.bordozer.jphoto.ui.controllers.users.photoAlbums.photos;

import com.bordozer.jphoto.core.general.base.AbstractGeneralModel;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.ui.elements.PhotoList;

public class UserPhotoAlbumPhotosModel extends AbstractGeneralModel {

    private UserPhotoAlbum photoAlbum;
    private PhotoList photoList;

    public UserPhotoAlbum getPhotoAlbum() {
        return photoAlbum;
    }

    public void setPhotoAlbum(final UserPhotoAlbum photoAlbum) {
        this.photoAlbum = photoAlbum;
    }

    public void setPhotoList(final PhotoList photoList) {
        this.photoList = photoList;
    }

    public PhotoList getPhotoList() {
        return photoList;
    }
}
