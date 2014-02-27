package core.general.menus.photo.commands;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.photo.Photo;
import utils.TranslatorUtils;

public class PhotoMenuItemDeleteCommand extends AbstractEntryMenuItemCommand<Photo> {

	public PhotoMenuItemDeleteCommand( final Photo menuEntry ) {
		super( menuEntry );
	}

	@Override
	public String getMenuText() {
		return TranslatorUtils.translate( "Delete photo" );
	}

	@Override
	public String getMenuCommand() {
		return String.format( "deletePhoto( %d ); return false;", getId() );
	}
}
