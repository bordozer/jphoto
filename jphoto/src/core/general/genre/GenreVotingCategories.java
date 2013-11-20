package core.general.genre;

import core.general.photo.PhotoVotingCategory;
import core.interfaces.Cacheable;

import java.util.List;

public class GenreVotingCategories implements Cacheable {

	private final int genreId;
	private List<PhotoVotingCategory> votingCategories;

	public GenreVotingCategories( final int genreId ) {
		this.genreId = genreId;
	}

	public int getGenreId() {
		return genreId;
	}

	public List<PhotoVotingCategory> getVotingCategories() {
		return votingCategories;
	}

	public void setVotingCategories( final List<PhotoVotingCategory> votingCategories ) {
		this.votingCategories = votingCategories;
	}
}
