package core.general.menus.comment.items;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import utils.TranslatorUtils;

public class CommentMenuItemEdit extends AbstractCommentMenuItem {

	public static final String BEAN_NAME = "commentEditMenu";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.MENU_ITEM_EDIT;
	}

	@Override
	public AbstractEntryMenuItemCommand initMenuItemCommand( final int commentId, final User userWhoIsCallingMenu ) {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Edit comment" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "editComment( %d ); return false;", commentId );
			}
		};
	}

	@Override
	public boolean isAccessibleForComment( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		return isUserWhoIsCallingMenuLogged( userWhoIsCallingMenu ) && isCommentLeftByUserWhoIsCallingMenu( photoComment, userWhoIsCallingMenu );
	}
}
