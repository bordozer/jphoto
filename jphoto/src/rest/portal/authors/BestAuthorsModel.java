package rest.portal.authors;

import java.util.List;

public class BestAuthorsModel {

	private int id = 1;

	private List<AuthorDTO> authorDTOs;

	private String title;

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public List<AuthorDTO> getAuthorDTOs() {
		return authorDTOs;
	}

	public void setAuthorDTOs( final List<AuthorDTO> authorDTOs ) {
		this.authorDTOs = authorDTOs;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( final String title ) {
		this.title = title;
	}
}
