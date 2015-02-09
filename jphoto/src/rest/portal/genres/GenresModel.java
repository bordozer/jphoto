package rest.portal.genres;

import java.util.List;

public class GenresModel {

	private int id = 1;
	private List<GenreDTO> genreDTOs;

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public List<GenreDTO> getGenreDTOs() {
		return genreDTOs;
	}

	public void setGenreDTOs( final List<GenreDTO> genreDTOs ) {
		this.genreDTOs = genreDTOs;
	}
}
