package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.ImageToImport;

public class RemotePhoto {

    private final RemotePhotoData remotePhotoData;
    private final ImageToImport imageToImport;

    public RemotePhoto(final RemotePhotoData remotePhotoData, final ImageToImport imageToImport) {
        this.remotePhotoData = remotePhotoData;
        this.imageToImport = imageToImport;
    }

    public RemotePhotoData getRemotePhotoData() {
        return remotePhotoData;
    }

    public ImageToImport getImageToImport() {
        return imageToImport;
    }

    @Override
    public String toString() {
        return String.format("%s", remotePhotoData);
    }
}
