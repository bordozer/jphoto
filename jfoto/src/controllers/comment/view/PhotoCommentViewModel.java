package controllers.comment.view;

import controllers.comment.edit.PhotoCommentInfo;

public class PhotoCommentViewModel {

	private final PhotoCommentInfo photoCommentInfo;

	public PhotoCommentViewModel( final PhotoCommentInfo photoCommentInfo ) {
		this.photoCommentInfo = photoCommentInfo;
	}

	public PhotoCommentInfo getPhotoCommentInfo() {
		return photoCommentInfo;
	}
}
