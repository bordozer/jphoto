package ui.controllers.users.card;

import ui.controllers.users.genreRank.VotingModel;
import ui.userRankIcons.UserRankIconContainer;

public class UserCardGenreInfo {

	private int photosQty;
	private VotingModel votingModel;
	private int votePointsForRankInGenre;
	private int votePointsToGetNextRankInGenre;
	private int userRankInGenre;
	private UserRankIconContainer userRankIconContainer;

	public int getPhotosQty() {
		return photosQty;
	}

	public void setPhotosQty( final int photosQty ) {
		this.photosQty = photosQty;
	}

	public int getVotePointsForRankInGenre() {
		return votePointsForRankInGenre;
	}

	public void setVotePointsForRankInGenre( final int votePointsForRankInGenre ) {
		this.votePointsForRankInGenre = votePointsForRankInGenre;
	}

	public int getVotePointsToGetNextRankInGenre() {
		return votePointsToGetNextRankInGenre;
	}

	public void setVotePointsToGetNextRankInGenre( final int votePointsToGetNextRankInGenre ) {
		this.votePointsToGetNextRankInGenre = votePointsToGetNextRankInGenre;
	}

	public int getUserRankInGenre() {
		return userRankInGenre;
	}

	public void setUserRankInGenre( final int userRankInGenre ) {
		this.userRankInGenre = userRankInGenre;
	}

	public UserRankIconContainer getUserRankIconContainer() {
		return userRankIconContainer;
	}

	public void setUserRankIconContainer( final UserRankIconContainer userRankIconContainer ) {
		this.userRankIconContainer = userRankIconContainer;
	}

	public VotingModel getVotingModel() {
		return votingModel;
	}

	public void setVotingModel( final VotingModel votingModel ) {
		this.votingModel = votingModel;
	}
}
