package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;

import java.util.Date;
import java.util.List;

public class ImageToImportData {

    private final ImageToImport imageToImport;

    private String name;

    private User user;
    private Date uploadTime;
    private String photoDescription;
    private String photoKeywords;
    private Photo photo;

    private String remoteUserId;
    private String remoteUserName;
    private int remotePhotoId;
    private List<RemotePhotoSiteSeries> remotePhotoSiteSeries;

    public ImageToImportData(final ImageToImport imageToImport) {
        this.imageToImport = imageToImport;
    }

    public ImageToImport getImageToImport() {
        return imageToImport;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(final Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(final String photoDescription) {
        this.photoDescription = photoDescription;
    }

    public String getPhotoKeywords() {
        return photoKeywords;
    }

    public void setPhotoKeywords(final String photoKeywords) {
        this.photoKeywords = photoKeywords;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(final Photo photo) {
        this.photo = photo;
    }

    public String getRemoteUserId() {
        return remoteUserId;
    }

    public void setRemoteUserId(final String remoteUserId) {
        this.remoteUserId = remoteUserId;
    }

    public String getRemoteUserName() {
        return remoteUserName;
    }

    public void setRemoteUserName(final String remoteUserName) {
        this.remoteUserName = remoteUserName;
    }

    public int getRemotePhotoId() {
        return remotePhotoId;
    }

    public void setRemotePhotoId(final int remotePhotoId) {
        this.remotePhotoId = remotePhotoId;
    }

    public void setRemotePhotoSiteSeries(final List<RemotePhotoSiteSeries> remotePhotoSiteSeries) {
        this.remotePhotoSiteSeries = remotePhotoSiteSeries;
    }

    public List<RemotePhotoSiteSeries> getRemotePhotoSiteSeries() {
        return remotePhotoSiteSeries;
    }

    @Override
    public String toString() {
        return String.format("Photo to import: %s", imageToImport);
    }

    @Override
    public int hashCode() {
        return 31 * imageToImport.hashCode();
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

        final ImageToImportData imageToImportData = (ImageToImportData) obj;
        return imageToImport == imageToImportData.getImageToImport();
    }
}

