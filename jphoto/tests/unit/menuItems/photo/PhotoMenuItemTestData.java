package menuItems.photo;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;

public class PhotoMenuItemTestData {

	private final User photoAuthor;
	private final User accessor;

	private final Genre genre;
	private final Photo photo;

	PhotoMenuItemTestData() {
		photoAuthor = new User( 222 );
		photoAuthor.setName( "Photo Author" );

		accessor = new User( 333 );
		accessor.setName( "Just a User" );

		genre = new Genre();
		genre.setId( 433 );
		genre.setName( "Photo category" );

		photo = new Photo();
		photo.setId( 567 );
		photo.setName( "The photo" );
		photo.setUserId( photoAuthor.getId() );
		photo.setGenreId( genre.getId() );
	}

	public User getPhotoAuthor() {
		return photoAuthor;
	}

	public User getAccessor() {
		return accessor;
	}

	public Photo getPhoto() {
		return photo;
	}

	public Genre getGenre() {
		return genre;
	}
}
