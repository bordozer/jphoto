package ui.controllers.photos.groupoperations;

public class GenreEntry {

	private final int genreId;
	private final String genreName;

	public GenreEntry( final int genreId, final String genreName ) {
		this.genreId = genreId;
		this.genreName = genreName;
	}

	public int getGenreId() {
		return genreId;
	}

	public String getGenreName() {
		return genreName;
	}
}
