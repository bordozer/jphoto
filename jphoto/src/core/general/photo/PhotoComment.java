package core.general.photo;

import core.general.base.AbstractBaseEntity;
import core.general.menus.PopupMenuAssignable;
import core.general.user.User;
import core.interfaces.Cacheable;

import java.util.Date;

public class PhotoComment extends AbstractBaseEntity implements Cacheable, PopupMenuAssignable {

	private int photoId;
	private User commentAuthor; // TODO: replace with UserId
	private int replyToCommentId;
	private String commentText;
	private Date creationTime;
	private Date readTime;
	private boolean commentDeleted;

	public PhotoComment() {
	}

	public PhotoComment( final PhotoComment comment ) {
		photoId = comment.getPhotoId();
		commentAuthor = comment.getCommentAuthor();
		replyToCommentId = comment.getReplyToCommentId();
		commentText = comment.getCommentText();
		creationTime = comment.getCreationTime();
		readTime = comment.getReadTime();
	}

	public int getPhotoId() {
		return photoId;
	}

	public void setPhotoId( final int photoId ) {
		this.photoId = photoId;
	}

	public User getCommentAuthor() {
		return commentAuthor;
	}

	public void setCommentAuthor( final User commentAuthor ) {
		this.commentAuthor = commentAuthor;
	}

	public int getReplyToCommentId() {
		return replyToCommentId;
	}

	public void setReplyToCommentId( final int replyToCommentId ) {
		this.replyToCommentId = replyToCommentId;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText( final String commentText ) {
		this.commentText = commentText;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime( final Date creationTime ) {
		this.creationTime = creationTime;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime( final Date readTime ) {
		this.readTime = readTime;
	}

	public boolean isCommentDeleted() {
		return commentDeleted;
	}

	public void setCommentDeleted( final boolean commentDeleted ) {
		this.commentDeleted = commentDeleted;
	}

	@Override
	public String toString() {
		return String.format( "Photo %d, comment %d", photoId, getId() );
	}
}
