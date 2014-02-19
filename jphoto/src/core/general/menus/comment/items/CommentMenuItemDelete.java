package core.general.menus.comment.items;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import utils.TranslatorUtils;

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

				if ( isCommentLeftByUserWhoIsCallingMenu( photoComment, userWhoIsCallingMenu ) ) {
					return TranslatorUtils.translate( "Delete your comment" );
				}

				if ( securityService.userOwnThePhoto( userWhoIsCallingMenu, photoComment.getPhotoId() ) ) {
					return TranslatorUtils.translate( "Delete comment (as photo author)" );
				}

				return TranslatorUtils.translate(  "Delete comment (ADMIN)" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "deleteComment( %d ); return false;", commentId );
			}
		};
	}

	@Override
	public boolean isAccessibleForComment( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		return securityService.userCanDeletePhotoComment( userWhoIsCallingMenu.getId(), photoComment.getId() );
	}
}
