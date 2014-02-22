package core.general.menus.photo.items;

import core.general.photo.Photo;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.security.Services;
import utils.TranslatorUtils;

public class PhotoMenuItemGoToAuthorPhotoByAlbum extends AbstractPhotoMenuItem {

	private final UserPhotoAlbum userPhotoAlbum;

	public PhotoMenuItemGoToAuthorPhotoByAlbum( final Photo photo, final User accessor, final Services services, final UserPhotoAlbum userPhotoAlbum ) {
		super( photo, accessor, services );

		this.userPhotoAlbum = userPhotoAlbum;
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_ALBUM;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {

		final User photoAuthor = getPhotoAuthor( menuEntry );

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
	public boolean isAccessibleFor( final Photo photo, final User userWhoIsCallingMenu ) {
		if ( hideMenuItemBecauseEntryOfMenuCaller( photo, userWhoIsCallingMenu ) ) {
			return false;
		}

		return super.isAccessibleFor( photo, userWhoIsCallingMenu ) && getUserPhotoAlbumPhotosQty() > 1;
	}

	private int getUserPhotoAlbumPhotosQty() {
		return services.getUserPhotoAlbumService().getUserPhotoAlbumPhotosQty( userPhotoAlbum.getId() );
	}
}
