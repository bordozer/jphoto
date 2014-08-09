package ui.services.menu.entry.items.comment.admin;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.comment.AbstractCommentMenuItem;

public class CommentMenuItemDeleteAdmin extends AbstractCommentMenuItem {

	public CommentMenuItemDeleteAdmin( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_MENU_ITEM_DELETE;
	}

	@Override
	public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<PhotoComment>( menuEntry, accessor, services ) {

			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "CommentMenuItemDelete: Delete comment", getLanguage() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "deleteComment( %d );", getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {

		if ( menuEntry.isCommentDeleted() ) {
			return false;
		}

		return getSecurityService().isSuperAdminUser( accessor );
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_ADMIN_CSS_CLASS;
	}
}
