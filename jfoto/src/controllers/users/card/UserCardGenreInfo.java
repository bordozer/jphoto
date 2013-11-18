package controllers.users.card;

import controllers.users.genreRank.VotingModel;

public class UserCardGenreInfo {

	private int photosQty;
	private VotingModel votingModel;
	private int votePointsForRankInGenre;
	private int votePointsToGetNextRankInGenre;

	public int getPhotosQty() {
		return photosQty;
	}

	public void setPhotosQty( final int photosQty ) {
		this.photosQty = photosQty;
	}

	public VotingModel getVotingModel() {
		return votingModel;
	}

	public void setVotingModel( final VotingModel votingModel ) {
		this.votingModel = votingModel;
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
}
