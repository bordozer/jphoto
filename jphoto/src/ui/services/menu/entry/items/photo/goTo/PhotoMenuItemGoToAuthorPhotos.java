package ui.services.menu.entry.items.photo.goTo;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;

public class PhotoMenuItemGoToAuthorPhotos extends AbstractPhotoGoToAuthorPhotos {

	public PhotoMenuItemGoToAuthorPhotos( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {

		final User photoAuthor = getPhotoAuthor();

		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, accessor, services ) {

			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "PhotoMenuItem: $1: all photos ( $2 )", getLanguage(), photoAuthor.getNameEscaped(), String.valueOf( getPhotosQty() ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotos( %d );", photoAuthor.getId() );
			}
		};
	}

	@Override
	protected int getPhotosQty() {
		return getPhotoService().getPhotosCountByUser( menuEntry.getUserId() );
	}
}
