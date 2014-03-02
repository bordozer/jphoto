package core.general.menus.photo.commands;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.photo.Photo;
import core.services.security.Services;
import utils.TranslatorUtils;

public class PhotoMenuItemDeleteCommand extends AbstractEntryMenuItemCommand<Photo> {

	public PhotoMenuItemDeleteCommand( final Photo menuEntry, Services services ) {
		super( menuEntry, services );
	}

	@Override
	public String getMenuText() {
		return getTranslatorService().translate( "Delete photo" );
	}

	@Override
	public String getMenuCommand() {
		return String.format( "deletePhoto( %d ); return false;", getId() );
	}
}
