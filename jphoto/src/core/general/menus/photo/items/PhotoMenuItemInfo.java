package core.general.menus.photo.items;

import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;

public class PhotoMenuItemInfo extends AbstractPhotoMenuItem {

	public PhotoMenuItemInfo( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.PHOTO_INFO;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {
			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Photo info" );
			}

			@Override
			public String getMenuCommand() {
				final int photoId = getId();
				final String infoLink = services.getUrlUtilsService().getPhotoInfoLink( photoId );

				return String.format( "openPopupWindowCustom( '%s', 'photoInfo_%d', 460, 800, 100, 100 );", infoLink, photoId );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return true;
	}
}
