package core.general.menus.comment.operations;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.menus.comment.commands.CommentMenuItemEditCommand;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
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
	public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {
		return new CommentMenuItemEditCommand( menuEntry, getEntryMenuType() );
	}

	@Override
	public boolean isAccessibleFor() {
		return !menuEntry.isCommentDeleted() && UserUtils.isLoggedUser( accessor ) && getSecurityService().userOwnThePhotoComment( accessor, menuEntry );
	}
}
