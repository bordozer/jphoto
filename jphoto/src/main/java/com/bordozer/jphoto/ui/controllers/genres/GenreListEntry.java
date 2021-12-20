package com.bordozer.jphoto.ui.controllers.genres;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.PhotoPreviewWrapper;

public class GenreListEntry {

    private final Genre genre;
    private String genreNameTranslated;
    private PhotoPreviewWrapper photoPreviewWrapper;
    private String photosByGenreURL;
    private int photosCount;
    private String genreIconTitle;

    public GenreListEntry(final Genre genre) {
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenreNameTranslated(final String genreNameTranslated) {
        this.genreNameTranslated = genreNameTranslated;
    }

    public String getGenreNameTranslated() {
        return genreNameTranslated;
    }

    public void setPhotoPreviewWrapper(final PhotoPreviewWrapper photoPreviewWrapper) {
        this.photoPreviewWrapper = photoPreviewWrapper;
    }

    public PhotoPreviewWrapper getPhotoPreviewWrapper() {
        return photoPreviewWrapper;
    }

    public void setPhotosByGenreURL(final String photosByGenreURL) {
        this.photosByGenreURL = photosByGenreURL;
    }

    public String getPhotosByGenreURL() {
        return photosByGenreURL;
    }

    public void setPhotosCount(final int photosCount) {
        this.photosCount = photosCount;
    }

    public int getPhotosCount() {
        return photosCount;
    }

    public void setGenreIconTitle(final String genreIconTitle) {
        this.genreIconTitle = genreIconTitle;
    }

    public String getGenreIconTitle() {
        return genreIconTitle;
    }
}
