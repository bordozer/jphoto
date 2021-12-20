package com.bordozer.jphoto.core.general.user;

import com.bordozer.jphoto.core.general.genre.Genre;

public class UserPhotosByGenre {

    private final Genre genre;
    private int photosQty;

    public UserPhotosByGenre(final Genre genre) {
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }

    public int getPhotosQty() {
        return photosQty;
    }

    public void setPhotosQty(final int photosQty) {
        this.photosQty = photosQty;
    }

    @Override
    public String toString() {
        return String.format("%s: %d", genre, photosQty);
    }
}
