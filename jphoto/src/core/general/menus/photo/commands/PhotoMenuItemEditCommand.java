package core.general.menus.photo.commands;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.photo.Photo;
import utils.TranslatorUtils;

public class PhotoMenuItemEditCommand extends AbstractEntryMenuItemCommand<Photo> {

	public PhotoMenuItemEditCommand( final Photo photo ) {
		super( photo );
	}

	public String getMenuText() {
		return TranslatorUtils.translate( "Edit photo" );
	}

	@Override
	public String getMenuCommand() {
		return String.format( "editPhotoData( %d );", getId() );
	}
}
