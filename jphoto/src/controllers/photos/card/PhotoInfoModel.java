package controllers.photos.card;

import controllers.users.genreRank.VotingModel;
import core.general.photo.PhotoInfo;

public class PhotoInfoModel {

	private PhotoInfo photoInfo;
	private VotingModel votingModel;

	public PhotoInfo getPhotoInfo() {
		return photoInfo;
	}

	public void setPhotoInfo( final PhotoInfo photoInfo ) {
		this.photoInfo = photoInfo;
	}

	public VotingModel getVotingModel() {
		return votingModel;
	}

	public void setVotingModel( final VotingModel votingModel ) {
		this.votingModel = votingModel;
	}
}
