package com.bordozer.jphoto.ui.controllers.users.card;

import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;

public class MarksByCategoryInfo {

    private PhotoVotingCategory photoVotingCategory;
    private int sumMark;
    private int quantity;

    public PhotoVotingCategory getPhotoVotingCategory() {
        return photoVotingCategory;
    }

    public void setPhotoVotingCategory(final PhotoVotingCategory photoVotingCategory) {
        this.photoVotingCategory = photoVotingCategory;
    }

    public int getSumMark() {
        return sumMark;
    }

    public void setSumMark(final int sumMark) {
        this.sumMark = sumMark;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }
}
