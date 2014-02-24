package menuItems.photo;

import core.general.photo.Photo;
import core.general.user.User;

class PhotoMenuItemTestData {

	private final User photoAuthor;
	private final User justUser;

	private final Photo photo;

	PhotoMenuItemTestData() {
		photoAuthor = new User( 222 );
		photoAuthor.setName( "Photo Author" );

		justUser = new User( 333 );
		justUser.setName( "Just a User" );

		photo = new Photo();
		photo.setId( 567 );
		photo.setName( "The photo" );
		photo.setUserId( photoAuthor.getId() );
	}

	public User getPhotoAuthor() {
		return photoAuthor;
	}

	public User getJustUser() {
		return justUser;
	}

	public Photo getPhoto() {
		return photo;
	}
}
