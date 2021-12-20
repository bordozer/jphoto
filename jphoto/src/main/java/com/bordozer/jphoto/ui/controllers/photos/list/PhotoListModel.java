package com.bordozer.jphoto.ui.controllers.photos.list;

import com.bordozer.jphoto.core.general.base.AbstractGeneralModel;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserPhotosByGenre;
import com.bordozer.jphoto.ui.elements.PhotoList;
import com.bordozer.jphoto.utils.PhotoUtils;
import org.springframework.mobile.device.DeviceType;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoListModel extends AbstractGeneralModel {

    private DeviceType deviceType;
    private List<PhotoList> photoLists = newArrayList();

    private User user;
    private List<UserPhotosByGenre> userPhotosByGenres;

    private boolean showPhotoSearchForm = true;

    public List<PhotoList> getPhotoLists() {
        return photoLists;
    }

    public void addPhotoList(final PhotoList photoList) {
        if (photoList != null) {
            photoLists.add(photoList);
        }
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(final DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public boolean isMobileDevice() {
        return PhotoUtils.isMobileDevice(deviceType);
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public List<UserPhotosByGenre> getUserPhotosByGenres() {
        return userPhotosByGenres;
    }

    public void setUserPhotosByGenres(final List<UserPhotosByGenre> userPhotosByGenres) {
        this.userPhotosByGenres = userPhotosByGenres;
    }

    public boolean isShowPhotoSearchForm() {
        return showPhotoSearchForm;
    }

    public void setShowPhotoSearchForm(final boolean showPhotoSearchForm) {
        this.showPhotoSearchForm = showPhotoSearchForm;
    }
}
