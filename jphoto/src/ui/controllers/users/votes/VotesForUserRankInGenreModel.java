package ui.controllers.users.votes;

import core.general.base.AbstractGeneralModel;
import core.general.genre.Genre;
import core.general.user.User;

import java.util.List;
import java.util.Map;

public class VotesForUserRankInGenreModel extends AbstractGeneralModel {

	private User user;
	private Genre genre;

	private int sumPoints;

	private List<UserGenreRankViewEntry> userGenreRankViewEntries;
	private Map<Integer, Integer> ranksInGenrePointsMap;

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}

	public void setGenre( final Genre genre ) {
		this.genre = genre;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setSumPoints( final int sumPoints ) {
		this.sumPoints = sumPoints;
	}

	public int getSumPoints() {
		return sumPoints;
	}

	public List<UserGenreRankViewEntry> getUserGenreRankViewEntries() {
		return userGenreRankViewEntries;
	}

	public void setUserGenreRankViewEntries( final List<UserGenreRankViewEntry> userGenreRankViewEntries ) {
		this.userGenreRankViewEntries = userGenreRankViewEntries;
	}

	public Map<Integer, Integer> getRanksInGenrePointsMap() {
		return ranksInGenrePointsMap;
	}

	public void setRanksInGenrePointsMap( final Map<Integer, Integer> ranksInGenrePointsMap ) {
		this.ranksInGenrePointsMap = ranksInGenrePointsMap;
	}
}
