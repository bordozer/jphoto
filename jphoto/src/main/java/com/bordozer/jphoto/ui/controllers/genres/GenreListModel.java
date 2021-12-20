package com.bordozer.jphoto.ui.controllers.genres;

import com.bordozer.jphoto.core.general.base.AbstractGeneralPageModel;

import java.util.List;

public class GenreListModel extends AbstractGeneralPageModel {

    private final List<GenreListEntry> genreListEntries;

    public GenreListModel(final List<GenreListEntry> genreListEntries) {
        this.genreListEntries = genreListEntries;
    }

    public List<GenreListEntry> getGenreListEntries() {
        return genreListEntries;
    }
}
