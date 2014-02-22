package core.general.menus.comment.items;

import core.general.menus.AbstractEntryMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.EntryMenuType;
import core.general.menus.comment.ComplaintReasonType;
import core.services.security.Services;
import utils.TranslatorUtils;

public class CommentMenuItemComplaintSwordWords extends AbstractCommentComplaintMenuItem {

	public CommentMenuItemComplaintSwordWords( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.COMMENT_COMPLAINT_SWORD_WORDS;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Report sword words or offence" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "%s( %d, %d, %d, %d ); return false;", COMPLAINT_MESSAGE_JS_FUNCTION, EntryMenuType.COMMENT.getId(), getId(), accessor.getId(), ComplaintReasonType.COMMENT_SWORD_WORDS.getId() );
			}
		};
	}
}
