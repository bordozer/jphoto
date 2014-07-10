package ui.services.menu.entry.items.comment.complain;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.comment.ComplaintReasonType;

public class CommentMenuItemComplaintSwordWords extends AbstractCommentComplaintMenuItem {

	public CommentMenuItemComplaintSwordWords( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.COMMENT_COMPLAINT_SWORD_WORDS;
	}

	@Override
	protected ComplaintReasonType getComplainReasonType() {
		return ComplaintReasonType.COMMENT_SWORD_WORDS;
	}

	@Override
	protected String getMenuItemText() {
		return translate( "Report sword words or offence" );
	}
}
