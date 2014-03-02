package core.general.menus.comment.commands;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.photo.PhotoComment;
import core.services.security.Services;

public class CommentMenuItemEditCommand extends AbstractEntryMenuItemCommand<PhotoComment> {

	public CommentMenuItemEditCommand( final PhotoComment menuEntry, final Services services ) {
		super( menuEntry, services );
	}

	@Override
	public String getMenuText() {
		return getTranslatorService().translate( "Edit comment" );
	}

	@Override
	public String getMenuCommand() {
		return String.format( "editComment( %d ); return false;", getId() );
	}
}
