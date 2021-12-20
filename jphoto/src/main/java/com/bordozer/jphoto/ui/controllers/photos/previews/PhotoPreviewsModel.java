package com.bordozer.jphoto.ui.controllers.photos.previews;

import com.bordozer.jphoto.core.general.base.AbstractGeneralPageModel;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoPreview;
import com.bordozer.jphoto.core.general.photo.PhotoPreviewWrapper;
import com.bordozer.jphoto.core.general.user.User;

import java.util.List;

public class PhotoPreviewsModel extends AbstractGeneralPageModel {

    private Photo photo;
    private User photoAuthor;
    private Genre genre;

    private List<PhotoPreview> photoPreviews;

    private PhotoPreviewWrapper photoPreviewWrapper;

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(final Photo photo) {
        this.photo = photo;
    }

    public User getPhotoAuthor() {
        return photoAuthor;
    }

    public void setPhotoAuthor(final User photoAuthor) {
        this.photoAuthor = photoAuthor;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(final Genre genre) {
        this.genre = genre;
    }

    public List<PhotoPreview> getPhotoPreviews() {
        return photoPreviews;
    }

    public void setPhotoPreviews(final List<PhotoPreview> photoPreviews) {
        this.photoPreviews = photoPreviews;
    }

    public PhotoPreviewWrapper getPhotoPreviewWrapper() {
        return photoPreviewWrapper;
    }

    public void setPhotoPreviewWrapper(final PhotoPreviewWrapper photoPreviewWrapper) {
        this.photoPreviewWrapper = photoPreviewWrapper;
    }
}
