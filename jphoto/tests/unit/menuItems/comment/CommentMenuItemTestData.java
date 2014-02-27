package menuItems.comment;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;

public class CommentMenuItemTestData {

	private final User commentAuthor;
	private final User photoAuthor;
	private final User accessor;

	private final Photo photo;

	private final PhotoComment comment;
	private final Genre genre;

	CommentMenuItemTestData() {
		commentAuthor = new User( 111 );
		commentAuthor.setName( "Comment Author" );

		photoAuthor = new User( 222 );
		photoAuthor.setName( "Photo Author" );

		accessor = new User( 333 );
		accessor.setName( "Just a User" );

		genre = new Genre();
		genre.setId( 400 );

		photo = new Photo();
		photo.setId( 567 );
		photo.setName( "The photo" );
		photo.setUserId( photoAuthor.getId() );
		photo.setGenreId( genre.getId() );

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

	public User getAccessor() {
		return accessor;
	}

	public Photo getPhoto() {
		return photo;
	}

	public PhotoComment getComment() {
		return comment;
	}

	public Genre getGenre() {
		return genre;
	}
}
