package core.general.menus.comment.operations;

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
	public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<PhotoComment>( menuEntry ) {

			@Override
			public String getMenuText() {
				if ( isCommentLeftByAccessor() ) {
					return TranslatorUtils.translate( "Delete comment" );
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

		if ( menuEntry.isCommentDeleted() ) {
			return false;
		}

		if ( ! UserUtils.isLoggedUser( accessor ) ) {
			return false;
		}

		if ( UserUtils.isUserOwnThePhoto( accessor, getPhoto() ) ) {
			return true;
		}

		// TODO: should be allowed deletion of admin messages?

		return isCommentLeftByAccessor();
	}
}
