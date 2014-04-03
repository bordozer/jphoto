package ui.controllers.users.genreRank;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import org.springframework.validation.BindingResult;

public class UserGenreRankVotingModel {

	private User voter;
	private User user;
	private Genre genre;
	private boolean isVotingForRankIncreasing;

	private VotingModel votingModel;

	private BindingResult bindingResult;

	private Photo photo;

	public User getVoter() {
		return voter;
	}

	public void setVoter( final User voter ) {
		this.voter = voter;
	}

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre( final Genre genre ) {
		this.genre = genre;
	}

	public VotingModel getVotingModel() {
		return votingModel;
	}

	public void setVotingModel( final VotingModel votingModel ) {
		this.votingModel = votingModel;
	}

	public void setBindingResult( final BindingResult bindingResult ) {
		this.bindingResult = bindingResult;
	}

	public BindingResult getBindingResult() {
		return bindingResult;
	}

	public boolean isVotingForRankIncreasing() {
		return isVotingForRankIncreasing;
	}

	public void setVotingForRankIncreasing( final boolean votingForRankIncreasing ) {
		isVotingForRankIncreasing = votingForRankIncreasing;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto( final Photo photo ) {
		this.photo = photo;
	}
}
