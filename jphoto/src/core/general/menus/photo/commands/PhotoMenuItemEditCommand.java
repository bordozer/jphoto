package core.general.menus.photo.commands;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.photo.Photo;
import core.services.security.Services;

public class PhotoMenuItemEditCommand extends AbstractEntryMenuItemCommand<Photo> {

	public PhotoMenuItemEditCommand( final Photo photo, final Services services ) {
		super( photo, services );
	}

	public String getMenuText() {
		return getTranslatorService().translate( "Edit photo" );
	}

	@Override
	public String getMenuCommand() {
		return String.format( "editPhotoData( %d );", getId() );
	}
}
