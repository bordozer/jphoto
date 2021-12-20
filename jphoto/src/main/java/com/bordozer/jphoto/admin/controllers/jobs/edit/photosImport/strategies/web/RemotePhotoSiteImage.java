package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.RemotePhotoSiteSeries;

public class RemotePhotoSiteImage {

    private final String imageUrl;

    private RemotePhotoSiteSeries series;

    public RemotePhotoSiteImage(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public RemotePhotoSiteImage(final String imageUrl, final RemotePhotoSiteSeries series) {
        this.imageUrl = imageUrl;
        this.series = series;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public RemotePhotoSiteSeries getSeries() {
        return series;
    }

    public void setSeries(final RemotePhotoSiteSeries series) {
        this.series = series;
    }

    public boolean hasSeries() {
        return series != null;
    }

    @Override
    public String toString() {
        return imageUrl;
    }

    @Override
    public int hashCode() {
        return imageUrl.hashCode() * 31;
    }

    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }

        final RemotePhotoSiteImage remotePhotoSiteImage = (RemotePhotoSiteImage) obj;
        return imageUrl.equals(remotePhotoSiteImage.getImageUrl());
    }
}
