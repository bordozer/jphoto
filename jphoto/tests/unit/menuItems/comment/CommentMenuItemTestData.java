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
		commentAuthor.setName( "Comment Author" );

		photoAuthor = new User( 222 );
		photoAuthor.setName( "Photo Author" );

		justUser = new User( 333 );
		justUser.setName( "Just a User" );

		photo = new Photo();
		photo.setId( 567 );
		photo.setName( "The photo" );
		photo.setUserId( photoAuthor.getId() );

		comment = new PhotoComment();
		comment.setId( 345 );
		comment.setPhotoId( photo.getId() );
		comment.setCommentAuthor( commentAuthor );
		comment.setCommentText( "Comment text" );
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
