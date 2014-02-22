package core.general.menus.comment.items;

import core.general.menus.AbstractEntryMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.services.security.Services;
import utils.TranslatorUtils;

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

				if ( services.getSecurityService().userOwnThePhoto( accessor, menuEntry.getPhotoId() ) ) {
					return TranslatorUtils.translate( "Delete comment (as photo author)" );
				}

				return TranslatorUtils.translate(  "Delete comment (ADMIN)" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "deleteComment( %d ); return false;", getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return services.getSecurityService().userCanDeletePhotoComment( accessor.getId(), getId() );
	}
}
