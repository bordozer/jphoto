package core.general.menus.comment.items;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import utils.TranslatorUtils;
import utils.UserUtils;

public class CommentMenuItemDelete extends AbstractCommentMenuItem {

	public static final String BEAN_NAME = "commentDeleteMenu";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.MENU_ITEM_DELETE;
	}

	@Override
	public AbstractEntryMenuItemCommand initMenuItemCommand( final int commentId, final User userWhoIsCallingMenu ) {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				final PhotoComment photoComment = photoCommentService.load( commentId );
				return TranslatorUtils.translate( UserUtils.isUsersEqual( photoComment.getCommentAuthor(), userWhoIsCallingMenu ) ? "Delete your comment" : "Delete comment" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "deleteComment( %d ); return false;", commentId );
			}
		};
	}

	@Override
	public boolean isAccessibleForComment( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		if ( ! isUserWhoIsCallingMenuLogged( userWhoIsCallingMenu ) ) {
			return false;
		}

		return isSuperAdminUser( userWhoIsCallingMenu.getId() )
			   || isUserWhoIsCallingMenuOwnerOfThePhoto( photoComment, userWhoIsCallingMenu )
			   || isCommentLeftByUserWhoIsCallingMenu( photoComment, userWhoIsCallingMenu );
	}
}
