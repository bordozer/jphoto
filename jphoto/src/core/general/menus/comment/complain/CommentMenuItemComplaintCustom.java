package core.general.menus.comment.complain;

import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.ComplaintReasonType;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;

public class CommentMenuItemComplaintCustom extends AbstractCommentComplaintMenuItem {

	public CommentMenuItemComplaintCustom( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
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
		return translate( "Custom complaint" );
	}
}
