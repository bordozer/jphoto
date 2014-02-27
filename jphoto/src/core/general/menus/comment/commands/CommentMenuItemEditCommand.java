package core.general.menus.comment.commands;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.photo.PhotoComment;
import utils.TranslatorUtils;

public class CommentMenuItemEditCommand extends AbstractEntryMenuItemCommand<PhotoComment> {

	public CommentMenuItemEditCommand( final PhotoComment menuEntry, final EntryMenuOperationType entryMenuType ) {
		super( menuEntry, entryMenuType );
	}

	@Override
	public String getMenuText() {
		return TranslatorUtils.translate( "Edit comment" );
	}

	@Override
	public String getMenuCommand() {
		return String.format( "editComment( %d ); return false;", getId() );
	}
}
