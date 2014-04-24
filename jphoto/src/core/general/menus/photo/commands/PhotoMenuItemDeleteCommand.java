package core.general.menus.photo.commands;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;

public class PhotoMenuItemDeleteCommand extends AbstractEntryMenuItemCommand<Photo> {

	public PhotoMenuItemDeleteCommand( final Photo menuEntry, final User accessor, Services services ) {
		super( menuEntry, accessor, services );
	}

	@Override
	public String getMenuText() {
		return getTranslatorService().translate( "PhotoMenuItem: Delete photo", getLanguage() );
	}

	@Override
	public String getMenuCommand() {
		return String.format( "deletePhoto( %d );", getId() );
	}
}
