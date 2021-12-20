package com.bordozer.jphoto.core.general.data.photoList;

import com.bordozer.jphoto.core.general.data.PhotoListCriterias;
import com.bordozer.jphoto.core.general.photo.group.PhotoGroupOperationMenuContainer;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;
import com.bordozer.jphoto.ui.elements.PageTitleData;

import java.util.Date;

public class PhotoListData {

    protected PageTitleData titleData;
    protected final SqlIdsSelectQuery photoListQuery;
    protected PhotoListCriterias photoListCriterias;
    protected String linkToFullList;
    protected String photoListBottomText;
    protected Date photoRatingTimeFrom;
    protected Date photoRatingTimeTo;
    protected PhotoGroupOperationMenuContainer photoGroupOperationMenuContainer;

    public PhotoListData(final SqlIdsSelectQuery photoListQuery) {
        this.photoListQuery = photoListQuery;
    }

    public PageTitleData getTitleData() {
        return titleData;
    }

    public void setTitleData(final PageTitleData titleData) {
        this.titleData = titleData;
    }

    public SqlIdsSelectQuery getPhotoListQuery() {
        return photoListQuery;
    }

    public PhotoListCriterias getPhotoListCriterias() {
        return photoListCriterias;
    }

    public void setPhotoListCriterias(final PhotoListCriterias photoListCriterias) {
        this.photoListCriterias = photoListCriterias;
    }

    public String getLinkToFullList() {
        return linkToFullList;
    }

    public void setLinkToFullList(final String linkToFullList) {
        this.linkToFullList = linkToFullList;
    }

    public String getPhotoListBottomText() {
        return photoListBottomText;
    }

    public void setPhotoRatingTimeFrom(final Date photoRatingTimeFrom) {
        this.photoRatingTimeFrom = photoRatingTimeFrom;
    }

    public void setPhotoRatingTimeTo(final Date photoRatingTimeTo) {
        this.photoRatingTimeTo = photoRatingTimeTo;
    }

    public void setPhotoGroupOperationMenuContainer(final PhotoGroupOperationMenuContainer photoGroupOperationMenuContainer) {
        this.photoGroupOperationMenuContainer = photoGroupOperationMenuContainer;
    }
}
