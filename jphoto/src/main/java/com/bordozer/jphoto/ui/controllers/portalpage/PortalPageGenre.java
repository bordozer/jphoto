package com.bordozer.jphoto.ui.controllers.portalpage;

import com.bordozer.jphoto.core.general.genre.Genre;

public class PortalPageGenre {

    private Genre genre;
    private int total;
    private int today;

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(final Genre genre) {
        this.genre = genre;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(final int total) {
        this.total = total;
    }

    public int getToday() {
        return today;
    }

    public void setToday(final int today) {
        this.today = today;
    }
}
