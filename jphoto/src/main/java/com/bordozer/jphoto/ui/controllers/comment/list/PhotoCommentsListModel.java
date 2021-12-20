package com.bordozer.jphoto.ui.controllers.comment.list;

import com.bordozer.jphoto.core.general.base.AbstractGeneralPageModel;
import com.bordozer.jphoto.core.general.photo.PhotoPreviewWrapper;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.ui.controllers.comment.edit.PhotoCommentInfo;

import java.util.List;
import java.util.Map;

public class PhotoCommentsListModel extends AbstractGeneralPageModel {

    private User user;
    private Map<PhotoPreviewWrapper, List<PhotoCommentInfo>> photoCommentInfoMap;
    private boolean showPaging;

    public void setUser(final User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Map<PhotoPreviewWrapper, List<PhotoCommentInfo>> getPhotoCommentInfoMap() {
        return photoCommentInfoMap;
    }

    public void setPhotoCommentInfoMap(final Map<PhotoPreviewWrapper, List<PhotoCommentInfo>> photoCommentInfoMap) {
        this.photoCommentInfoMap = photoCommentInfoMap;
    }

    public boolean isShowPaging() {
        return showPaging;
    }

    public void setShowPaging(final boolean showPaging) {
        this.showPaging = showPaging;
    }
}
