package com.bordozer.jphoto.rest.portal.genres;

public class GenreDTO {

    private int genreId;
    private String genrePhotosLink;
    private int totalPhotos;
    private int todayPhotos;

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(final int genreId) {
        this.genreId = genreId;
    }

    public String getGenrePhotosLink() {
        return genrePhotosLink;
    }

    public void setGenrePhotosLink(final String genrePhotosLink) {
        this.genrePhotosLink = genrePhotosLink;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(final int totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public int getTodayPhotos() {
        return todayPhotos;
    }

    public void setTodayPhotos(final int todayPhotos) {
        this.todayPhotos = todayPhotos;
    }

    @Override
    public String toString() {
        return String.format("Genre %d", genreId);
    }
}
