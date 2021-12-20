package com.bordozer.jphoto.admin.controllers.votingCategories.list;

import com.bordozer.jphoto.core.general.base.AbstractGeneralModel;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class VotingCategoryListModel extends AbstractGeneralModel {

    private List<PhotoVotingCategory> photoVotingCategories = newArrayList();

    public List<PhotoVotingCategory> getPhotoVotingCategories() {
        return photoVotingCategories;
    }

    public void setPhotoVotingCategories(final List<PhotoVotingCategory> photoVotingCategories) {
        this.photoVotingCategories = photoVotingCategories;
    }

    @Override
    public void clear() {
        super.clear();
        photoVotingCategories = newArrayList();
    }
}
