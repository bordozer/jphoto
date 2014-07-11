package rest.users.rankInGenreVoting;

public class UserCardVotingAreaModel {

	private int userId;
	private int genreId;
	private int voterId;
	private int voterRankInGenreVotingPoints;
	private boolean uiVotingIsInaccessible;
	private String validationMessage;
	private int userLastVotingResult;
	private boolean votedForThisRank;

	public int getUserId() {
		return userId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public int getGenreId() {
		return genreId;
	}

	public void setGenreId( final int genreId ) {
		this.genreId = genreId;
	}

	public int getVoterId() {
		return voterId;
	}

	public void setVoterId( final int voterId ) {
		this.voterId = voterId;
	}

	public int getVoterRankInGenreVotingPoints() {
		return voterRankInGenreVotingPoints;
	}

	public void setVoterRankInGenreVotingPoints( final int voterRankInGenreVotingPoints ) {
		this.voterRankInGenreVotingPoints = voterRankInGenreVotingPoints;
	}

	public void setUiVotingIsInaccessible( final boolean uiVotingIsInaccessible ) {
		this.uiVotingIsInaccessible = uiVotingIsInaccessible;
	}

	public boolean isUiVotingIsInaccessible() {
		return uiVotingIsInaccessible;
	}

	public void setValidationMessage( final String validationMessage ) {
		this.validationMessage = validationMessage;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setUserLastVotingResult( final int userLastVotingResult ) {
		this.userLastVotingResult = userLastVotingResult;
	}

	public int getUserLastVotingResult() {
		return userLastVotingResult;
	}

	public void setVotedForThisRank( final boolean votedForThisRank ) {
		this.votedForThisRank = votedForThisRank;
	}

	public boolean isVotedForThisRank() {
		return votedForThisRank;
	}

	@Override
	public String toString() {
		return String.format( "Voting area: userId = %d; genreId = %d", userId, genreId );
	}
}
