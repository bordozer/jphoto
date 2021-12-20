package com.bordozer.jphoto.ui.controllers.comment.view;

import com.bordozer.jphoto.ui.controllers.comment.edit.PhotoCommentInfo;

public class PhotoCommentViewModel {

    private final PhotoCommentInfo photoCommentInfo;

    public PhotoCommentViewModel(final PhotoCommentInfo photoCommentInfo) {
        this.photoCommentInfo = photoCommentInfo;
    }

    public PhotoCommentInfo getPhotoCommentInfo() {
        return photoCommentInfo;
    }
}
