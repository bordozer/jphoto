package com.bordozer.jphoto.ui.controllers.photos.list;

import com.bordozer.jphoto.ui.controllers.photos.edit.GenreWrapper;
import org.springframework.validation.BindingResult;

import java.util.List;

public class PhotoFilterModel {

    private String filterPhotoName;
    private String filterGenreId;
    private boolean showPhotosWithNudeContent;

    private String filterAuthorName;
    private List<Integer> photoAuthorMembershipTypeIds;

    private String photosSortColumn;
    private String photosSortOrder;

    private List<GenreWrapper> genreWrappers;
    private BindingResult bindingResult;

    public String getFilterPhotoName() {
        return filterPhotoName;
    }

    public void setFilterPhotoName(final String filterPhotoName) {
        this.filterPhotoName = filterPhotoName;
    }

    public String getFilterGenreId() {
        return filterGenreId;
    }

    public void setFilterGenreId(final String filterGenreId) {
        this.filterGenreId = filterGenreId;
    }

    public boolean isShowPhotosWithNudeContent() {
        return showPhotosWithNudeContent;
    }

    public void setShowPhotosWithNudeContent(final boolean showPhotosWithNudeContent) {
        this.showPhotosWithNudeContent = showPhotosWithNudeContent;
    }

    public String getFilterAuthorName() {
        return filterAuthorName;
    }

    public void setFilterAuthorName(final String filterAuthorName) {
        this.filterAuthorName = filterAuthorName;
    }

    public List<Integer> getPhotoAuthorMembershipTypeIds() {
        return photoAuthorMembershipTypeIds;
    }

    public void setPhotoAuthorMembershipTypeIds(final List<Integer> photoAuthorMembershipTypeIds) {
        this.photoAuthorMembershipTypeIds = photoAuthorMembershipTypeIds;
    }

    public String getPhotosSortColumn() {
        return photosSortColumn;
    }

    public void setPhotosSortColumn(final String photosSortColumn) {
        this.photosSortColumn = photosSortColumn;
    }

    public String getPhotosSortOrder() {
        return photosSortOrder;
    }

    public void setPhotosSortOrder(final String photosSortOrder) {
        this.photosSortOrder = photosSortOrder;
    }

    public List<GenreWrapper> getGenreWrappers() {
        return genreWrappers;
    }

    public void setGenreWrappers(final List<GenreWrapper> genreWrappers) {
        this.genreWrappers = genreWrappers;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public void setBindingResult(final BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
