package core.general.menus.comment.items;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;
import utils.UserUtils;

public class CommentMenuItemEdit extends AbstractCommentMenuItem {

	public CommentMenuItemEdit( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.MENU_ITEM_EDIT;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Edit your comment" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "editComment( %d ); return false;", getId() );
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

		return getSecurityService().userOwnThePhotoComment( accessor, menuEntry );
	}
}
