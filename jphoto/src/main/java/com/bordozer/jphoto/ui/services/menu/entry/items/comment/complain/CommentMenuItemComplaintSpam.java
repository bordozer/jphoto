package com.bordozer.jphoto.ui.services.menu.entry.items.comment.complain;

import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.ComplaintReasonType;

public class CommentMenuItemComplaintSpam extends AbstractCommentComplaintMenuItem {

    public CommentMenuItemComplaintSpam(final PhotoComment photoComment, final User accessor, final Services services) {
        super(photoComment, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.COMMENT_COMPLAINT_SPAM;
    }

    @Override
    protected ComplaintReasonType getComplainReasonType() {
        return ComplaintReasonType.COMMENT_SPAM;
    }

    @Override
    protected String getMenuItemText() {
        return translate("CommentMenuItemComplaint: Spam");
    }
}
