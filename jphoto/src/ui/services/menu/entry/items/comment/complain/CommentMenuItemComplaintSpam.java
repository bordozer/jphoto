package ui.services.menu.entry.items.comment.complain;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.comment.ComplaintReasonType;

public class CommentMenuItemComplaintSpam extends AbstractCommentComplaintMenuItem {

	public CommentMenuItemComplaintSpam( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
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
		return translate( "CommentMenuItemComplaint: Spam" );
	}
}
