package ui.services.menu.entry.items.comment.commands;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;

public class CommentMenuItemEditCommand extends AbstractEntryMenuItemCommand<PhotoComment> {

	public CommentMenuItemEditCommand( final PhotoComment menuEntry, final User accessor, final Services services ) {
		super( menuEntry, accessor, services );
	}

	@Override
	public String getMenuText() {
		return getTranslatorService().translate( "Edit comment", getLanguage() );
	}

	@Override
	public String getMenuCommand() {
		return String.format( "editComment( %d );", getId() );
	}
}
