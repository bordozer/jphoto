package json.users.rankInGenreVoting;

public class UserCardVotingAreaModel {

	private int userId;
	private int genreId;
	private int voterId;
	private int voterRankInGenreVotingPoints;

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

	@Override
	public String toString() {
		return String.format( "Voting area: userId = %d; genreId = %d", userId, genreId );
	}
}
