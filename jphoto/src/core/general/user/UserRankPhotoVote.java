package core.general.user;

import core.general.genre.Genre;

import java.util.Date;

public class UserRankPhotoVote {

	private final User voter;
	private final Genre genre;

	private int votePoints;
	private Date voteTime;

	public UserRankPhotoVote( final User voter, final Genre genre ) {
		this.voter = voter;
		this.genre = genre;
	}

	public User getVoter() {
		return voter;
	}

	public Genre getGenre() {
		return genre;
	}

	public int getVotePoints() {
		return votePoints;
	}

	public void setVotePoints( final int votePoints ) {
		this.votePoints = votePoints;
	}

	public Date getVoteTime() {
		return voteTime;
	}

	public void setVoteTime( final Date voteTime ) {
		this.voteTime = voteTime;
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", voter, voteTime );
	}
}
