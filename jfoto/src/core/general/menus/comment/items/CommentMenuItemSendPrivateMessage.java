package core.general.menus.comment.items;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import utils.TranslatorUtils;
import utils.UserUtils;

public class CommentMenuItemSendPrivateMessage extends AbstractCommentMenuItem {

	public static final String BEAN_NAME = "commentMenuItemSendPrivateMessage";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.SEND_PRIVATE_MESSAGE;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int commentId, final User userWhoIsCallingMenu ) {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			private User commentAuthor = getCommentAuthor( commentId );

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Send private message to $1", commentAuthor.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "sendPrivateMessage( %d, %d, '%s' );", userWhoIsCallingMenu.getId(), commentAuthor.getId(), commentAuthor.getNameEscaped() );
			}
		};
	}

	@Override
	public boolean isAccessibleForComment( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		if ( isSuperAdminUser( userWhoIsCallingMenu ) && ! UserUtils.isUsersEqual( userWhoIsCallingMenu, photoComment.getCommentAuthor() ) ) {
			return true;
		}

		final boolean userInBlackListOfUser = favoritesService.isUserInBlackListOfUser( photoComment.getCommentAuthor().getId(), userWhoIsCallingMenu.getId() );

		if ( UserUtils.isUsersEqual( userWhoIsCallingMenu, getPhotoAuthor( photoComment ) ) && !userInBlackListOfUser ) {
			return true;
		}

		return isUserWhoIsCallingMenuLogged( userWhoIsCallingMenu )
			   && super.isAccessibleForComment( photoComment, userWhoIsCallingMenu )
			   && ! isCommentOfMenuCaller( photoComment, userWhoIsCallingMenu )
			   && !userInBlackListOfUser;
	}
}
