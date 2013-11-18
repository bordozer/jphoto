package core.general.menus.photo.items;

import core.general.photo.Photo;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.user.userAlbums.UserPhotoAlbum;
import utils.TranslatorUtils;

public class PhotoMenuItemGoToAuthorPhotoByAlbum extends AbstractPhotoMenuItem {

	public static final String BEAN_NAME = "photoMenuItemGoToAuthorPhotoByAlbum";

	private UserPhotoAlbum userPhotoAlbum;

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_ALBUM;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int photoId, final User userWhoIsCallingMenu ) {
		final User photoAuthor = getPhotoAuthor( photoId );
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "$1: photos from album '$2' ( $3 )"
					, photoAuthor.getNameEscaped(), userPhotoAlbum.getName(), String.valueOf( getUserPhotoAlbumPhotosQty() ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotosByAlbum( %d, %d );", photoAuthor.getId(), userPhotoAlbum.getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleForPhoto( final Photo photo, final User userWhoIsCallingMenu ) {
		if ( hideMenuItemBecauseEntryOfMenuCaller( photo, userWhoIsCallingMenu ) ) {
			return false;
		}

		return super.isAccessibleForPhoto( photo, userWhoIsCallingMenu ) && getUserPhotoAlbumPhotosQty() > 1;
	}

	private int getUserPhotoAlbumPhotosQty() {
		return userPhotoAlbumService.getUserPhotoAlbumPhotosQty( userPhotoAlbum.getId() );
	}

	public void setUserPhotoAlbum( final UserPhotoAlbum userPhotoAlbum ) {
		this.userPhotoAlbum = userPhotoAlbum;
	}
}
