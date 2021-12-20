package com.bordozer.jphoto.ui.services.menu.entry.items.comment.complain;

import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.ComplaintReasonType;

public class CommentMenuItemComplaintCustom extends AbstractCommentComplaintMenuItem {

    public CommentMenuItemComplaintCustom(final PhotoComment photoComment, final User accessor, final Services services) {
        super(photoComment, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.COMMENT_COMPLAINT_CUSTOM;
    }

    @Override
    protected ComplaintReasonType getComplainReasonType() {
        return ComplaintReasonType.COMMENT_CUSTOM_COMPLAINT;
    }

    @Override
    protected String getMenuItemText() {
        return translate("CommentMenuItemComplaint: Custom complaint");
    }
}
