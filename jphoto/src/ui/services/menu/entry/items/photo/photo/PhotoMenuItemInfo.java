package ui.services.menu.entry.items.photo.photo;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

public class PhotoMenuItemInfo extends AbstractPhotoMenuItem {

	public PhotoMenuItemInfo( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.PHOTO_INFO;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, accessor, services ) {
			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "PhotoMenuItem: Photo info", getLanguage() );
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