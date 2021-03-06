package com.bordozer.jphoto.rest.photo.upload.category;

import com.bordozer.jphoto.rest.photo.upload.category.allowance.PhotoUploadDescription;

import java.util.List;

public class PhotoUploadAllowanceDTO {

    private int useId;
    private int genreId;
    private List<PhotoUploadDescription> photoUploadAllowance;

    public void setUseId(final int useId) {
        this.useId = useId;
    }

    public int getUseId() {
        return useId;
    }

    public void setGenreId(final int genreId) {
        this.genreId = genreId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setPhotoUploadAllowance(final List<PhotoUploadDescription> photoUploadAllowance) {
        this.photoUploadAllowance = photoUploadAllowance;
    }

    public List<PhotoUploadDescription> getPhotoUploadAllowance() {
        return photoUploadAllowance;
    }
}
