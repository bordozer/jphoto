package menuItems.comment;

import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;

class CommentMenuItemTestData {

	private final User commentAuthor;
	private final User photoAuthor;
	private final User justUser;

	private final Photo photo;

	private final PhotoComment comment;

	CommentMenuItemTestData() {
		commentAuthor = new User( 111 );
		commentAuthor.setName( "commentAuthor" );

		photoAuthor = new User( 222 );
		commentAuthor.setName( "photoAuthor" );

		justUser = new User( 333 );
		commentAuthor.setName( "justUser" );

		photo = new Photo();
		photo.setId( 567 );
		photo.setName( "The photo" );
		photo.setUserId( photoAuthor.getId() );

		comment = new PhotoComment();
		comment.setId( 345 );
		comment.setPhotoId( photo.getId() );
		comment.setCommentAuthor( commentAuthor );
	}

	public User getCommentAuthor() {
		return commentAuthor;
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

	public PhotoComment getComment() {
		return comment;
	}
}
