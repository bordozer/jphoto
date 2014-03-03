package core.general.menus.comment.complain;

import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.ComplaintReasonType;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;

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
		return services.getTranslatorService().translate( "Report sword words or offence" );
	}
}
