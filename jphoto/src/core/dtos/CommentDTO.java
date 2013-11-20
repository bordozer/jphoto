package core.dtos;

import core.general.photo.PhotoComment;

public class CommentDTO {

	final private int commentId;
	private String commentText;
	private String errorMessage;

	public CommentDTO( final int commentId ) {
		this.commentId = commentId;
	}

	public CommentDTO( final int commentId, final String commentText ) {
		this.commentId = commentId;
		this.commentText = commentText;
	}

	public CommentDTO( final PhotoComment photoComment ) {
		this( photoComment.getId(), photoComment.getCommentText() );
	}

	public int getCommentId() {
		return commentId;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText( final String commentText ) {
		this.commentText = commentText;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage( final String errorMessage ) {
		this.errorMessage = errorMessage;
	}
}
