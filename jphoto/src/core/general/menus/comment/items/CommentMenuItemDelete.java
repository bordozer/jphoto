package core.general.menus.comment.items;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;
import utils.UserUtils;

public class CommentMenuItemDelete extends AbstractCommentMenuItem {

	public CommentMenuItemDelete( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.MENU_ITEM_DELETE;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				if ( isCommentLeftByUserWhoIsCallingMenu() ) {
					return TranslatorUtils.translate( "Delete your comment" );
				}

				return TranslatorUtils.translate( "Delete comment (as photo author)" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "deleteComment( %d ); return false;", getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return ! menuEntry.isCommentDeleted() && UserUtils.isLoggedUser( accessor ) && ( ( UserUtils.isUserOwnThePhoto( accessor, getPhoto() ) || UserUtils.isUsersEqual( accessor, getCommentAuthor() ) ) );
	}
}
