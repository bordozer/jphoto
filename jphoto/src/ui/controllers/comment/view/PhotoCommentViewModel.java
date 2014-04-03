package ui.controllers.comment.view;

import ui.controllers.comment.edit.PhotoCommentInfo;

public class PhotoCommentViewModel {

	private final PhotoCommentInfo photoCommentInfo;

	public PhotoCommentViewModel( final PhotoCommentInfo photoCommentInfo ) {
		this.photoCommentInfo = photoCommentInfo;
	}

	public PhotoCommentInfo getPhotoCommentInfo() {
		return photoCommentInfo;
	}
}
