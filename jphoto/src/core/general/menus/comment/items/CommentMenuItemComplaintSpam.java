package core.general.menus.comment.items;

import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.EntryMenuType;
import core.general.menus.comment.ComplaintReasonType;
import utils.TranslatorUtils;

public class CommentMenuItemComplaintSpam extends AbstractCommentComplaintMenuItem {

	public static final String BEAN_NAME = "commentComplaintSpamMenu";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.COMMENT_COMPLAINT_SPAM;
	}

	@Override
	public AbstractEntryMenuItemCommand initMenuItemCommand( final int commentId, final User userWhoIsCallingMenu ) {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Report spam" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "%s( %d, %d, %d, %d ); return false;", COMPLAINT_MESSAGE_JS_FUNCTION, EntryMenuType.COMMENT.getId(), commentId, userWhoIsCallingMenu.getId(), ComplaintReasonType.COMMENT_SPAM.getId() );
			}
		};
	}
}
