package ui.services.menu.entry.items.photo.commands;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

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
		return String.format( "deletePhotoFromContextMenu();" );
	}
}
