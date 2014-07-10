package ui.services.menu.entry.items.comment.admin;

import core.general.configuration.ConfigurationKey;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.comment.AbstractCommentMenuItem;
import ui.services.menu.entry.items.comment.commands.CommentMenuItemEditCommand;

public class CommentMenuItemEditAdmin extends AbstractCommentMenuItem {

	public CommentMenuItemEditAdmin( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_MENU_ITEM_EDIT;
	}

	@Override
	public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {
		return new CommentMenuItemEditCommand( menuEntry, accessor, services );
	}

	@Override
	public boolean isAccessibleFor() {
		return !menuEntry.isCommentDeleted()
			   && getSecurityService().isSuperAdminUser( accessor )
			   && services.getConfigurationService().getBoolean( ConfigurationKey.ADMIN_CAN_EDIT_PHOTO_COMMENTS );
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_ADMIN_CSS_CLASS;
	}
}
