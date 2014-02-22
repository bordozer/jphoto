package core.general.menus.comment.items;

import core.general.menus.AbstractEntryMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.services.security.Services;
import utils.TranslatorUtils;

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
				if ( isCommentLeftByUserWhoIsCallingMenu() ) {
					return TranslatorUtils.translate( "Edit your comment" );
				}

				return TranslatorUtils.translate(  "Edit comment (ADMIN)" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "editComment( %d ); return false;", getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return services.getSecurityService().userCanEditPhotoComment( accessor, menuEntry );
	}
}
