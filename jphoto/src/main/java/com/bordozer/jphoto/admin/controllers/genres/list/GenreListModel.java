package com.bordozer.jphoto.admin.controllers.genres.list;

import com.bordozer.jphoto.core.general.base.AbstractGeneralModel;
import com.bordozer.jphoto.core.general.genre.Genre;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class GenreListModel extends AbstractGeneralModel {

    private List<Genre> genreList = newArrayList();
    private Map<Integer, Integer> photosByGenreMap = newHashMap();

    private BindingResult bindingResult;

    private int systemMinMarksToBeInTheBestPhotoOfGenre;

    public List<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(final List<Genre> genreList) {
        this.genreList = genreList;
    }

    @Override
    public BindingResult getBindingResult() {
        return bindingResult;
    }

    @Override
    public void setBindingResult(final BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public Map<Integer, Integer> getPhotosByGenreMap() {
        return photosByGenreMap;
    }

    public void setPhotosByGenreMap(final Map<Integer, Integer> photosByGenreMap) {
        this.photosByGenreMap = photosByGenreMap;
    }

    public int getSystemMinMarksToBeInTheBestPhotoOfGenre() {
        return systemMinMarksToBeInTheBestPhotoOfGenre;
    }

    public void setSystemMinMarksToBeInTheBestPhotoOfGenre(final int systemMinMarksToBeInTheBestPhotoOfGenre) {
        this.systemMinMarksToBeInTheBestPhotoOfGenre = systemMinMarksToBeInTheBestPhotoOfGenre;
    }

    @Override
    public void clear() {
        super.clear();
        genreList = newArrayList();
    }
}
