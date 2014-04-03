package core.general.menus.comment.complain;

import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.ComplaintReasonType;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;

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
		return services.getTranslatorService().translate( "Report spam", getLanguage() );
	}
}
