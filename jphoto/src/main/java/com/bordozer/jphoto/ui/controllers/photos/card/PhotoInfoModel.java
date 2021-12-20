package com.bordozer.jphoto.ui.controllers.photos.card;

import com.bordozer.jphoto.core.general.photo.PhotoInfo;
import com.bordozer.jphoto.ui.controllers.users.genreRank.VotingModel;

public class PhotoInfoModel {

    private PhotoInfo photoInfo;
    private VotingModel votingModel;

    public PhotoInfo getPhotoInfo() {
        return photoInfo;
    }

    public void setPhotoInfo(final PhotoInfo photoInfo) {
        this.photoInfo = photoInfo;
    }

    public VotingModel getVotingModel() {
        return votingModel;
    }

    public void setVotingModel(final VotingModel votingModel) {
        this.votingModel = votingModel;
    }
}
