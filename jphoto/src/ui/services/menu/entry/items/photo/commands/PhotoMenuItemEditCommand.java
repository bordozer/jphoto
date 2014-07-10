package ui.services.menu.entry.items.photo.commands;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;

public class PhotoMenuItemEditCommand extends AbstractEntryMenuItemCommand<Photo> {

	public PhotoMenuItemEditCommand( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	public String getMenuText() {
		return getTranslatorService().translate( "PhotoMenuItem: Edit photo", getLanguage() );
	}

	@Override
	public String getMenuCommand() {
		return String.format( "editPhotoData( %d );", getId() );
	}
}
