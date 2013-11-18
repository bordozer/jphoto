package core.general.user;

import core.general.photo.Photo;

import java.util.Date;

public class UserRankInGenreVoting {

	private int userId;
	private int voterId;
	private int genreId;
	private int userRankWhenVoting;
	private int points;
	private Date votingTime;

	private Photo photo;

	public int getUserId() {
		return userId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public int getVoterId() {
		return voterId;
	}

	public void setVoterId( final int voterId ) {
		this.voterId = voterId;
	}

	public int getGenreId() {
		return genreId;
	}

	public void setGenreId( final int genreId ) {
		this.genreId = genreId;
	}

	public int getUserRankWhenVoting() {
		return userRankWhenVoting;
	}

	public void setUserRankWhenVoting( final int userRankWhenVoting ) {
		this.userRankWhenVoting = userRankWhenVoting;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints( final int points ) {
		this.points = points;
	}

	public Date getVotingTime() {
		return votingTime;
	}

	public void setVotingTime( final Date votingTime ) {
		this.votingTime = votingTime;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto( final Photo photo ) {
		this.photo = photo;
	}
}
