package core.general.menus.comment.items;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;

public class CommentAdminSubMenuItemLockUser extends AbstractCommentMenuItem {

	public CommentAdminSubMenuItemLockUser( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {

		final User commentAuthor = menuEntry.getCommentAuthor();

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {
			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Lock comment author: $1", commentAuthor.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "adminLockUser( %d, '%s' ); return false;", commentAuthor.getId(), commentAuthor.getNameEscaped() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {

		if ( ! isAccessorSuperAdmin() ) {
			return false;
		}

		return !isCommentLeftByAccessor();
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_CSS_CLASS_ADMIN;
	}
}
