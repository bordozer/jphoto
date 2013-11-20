package core.general.menus.photo.items;

import core.general.photo.Photo;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import utils.TranslatorUtils;

public class PhotoMenuItemGoToAuthorPhotos extends AbstractPhotoMenuItem {

	public static final String BEAN_NAME = "photoMenuItemGoToAuthorPhotos";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int photoId, final User userWhoIsCallingMenu ) {
		final User photoAuthor = getPhotoAuthor( photoId );
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				final int photoQtyByUser = getPhotoQtyByUser( photoAuthor.getId() );
				return TranslatorUtils.translate( "$1: all photos ( $2 )", photoAuthor.getNameEscaped(), String.valueOf( photoQtyByUser ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotos( %d );", photoAuthor.getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleForPhoto( final Photo photo, final User userWhoIsCallingMenu ) {
		if ( hideMenuItemBecauseEntryOfMenuCaller( photo, userWhoIsCallingMenu ) ) {
			return false;
		}

		return super.isAccessibleForPhoto( photo, userWhoIsCallingMenu ) && getPhotoQtyByUser( photo.getUserId() ) > 1;
	}

	private int getPhotoQtyByUser( final int userId ) {
		return photoService.getPhotoQtyByUser( userId );
	}
}
