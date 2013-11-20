package core.general.menus.photo.items;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import utils.TranslatorUtils;

public class PhotoMenuItemInfo extends AbstractPhotoMenuItem {

	public static final String BEAN_NAME = "photoMenuItemInfo";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.PHOTO_INFO;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int photoId, final User userWhoIsCallingMenu ) {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {
			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "Photo info" );
			}

			@Override
			public String getMenuCommand() {
				final String infoLink = urlUtilsService.getPhotoInfoLink( photoId );
				return String.format( "openPopupWindowCustom( '%s', 'photoInfo_%d', 460, 800, 100, 100 );", infoLink, photoId );
			}
		};
	}

	@Override
	public boolean isAccessibleForPhoto( final Photo photo, final User userWhoIsCallingMenu ) {
		return true;
	}
}
