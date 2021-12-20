package com.bordozer.jphoto.admin.controllers.jobs.edit.favorites;

import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractAdminJobModel;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class FavoritesJobModel extends AbstractAdminJobModel {

    public final static String ACTIONS_QTY_FORM_CONTROL = "actionsQty";
    public final static String PHOTO_QTY_FORM_CONTROL = "photoQty";
    public final static String FAVORITE_ENTRIES_IDS_FORM_CONTROL = "favoriteEntriesIds";

    private String actionsQty;
    private String photoQty;
    private List<String> favoriteEntriesIds = newArrayList();

    public String getActionsQty() {
        return actionsQty;
    }

    public void setActionsQty(final String actionsQty) {
        this.actionsQty = actionsQty;
    }

    public String getPhotoQty() {
        return photoQty;
    }

    public void setPhotoQty(final String photoQty) {
        this.photoQty = photoQty;
    }

    public List<String> getFavoriteEntriesIds() {
        return favoriteEntriesIds;
    }

    public void setFavoriteEntriesIds(final List<String> favoriteEntriesIds) {
        this.favoriteEntriesIds = favoriteEntriesIds;
    }
}
