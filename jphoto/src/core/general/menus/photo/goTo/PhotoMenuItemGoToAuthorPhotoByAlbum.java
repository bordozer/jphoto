package core.general.menus.photo.goTo;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.security.Services;

public class PhotoMenuItemGoToAuthorPhotoByAlbum extends AbstractPhotoGoToAuthorPhotos {

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
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {

		final User photoAuthor = getPhotoAuthor();

		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, accessor, services ) {

			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "$1: photos from album '$2' ( $3 )", getLanguage(), photoAuthor.getNameEscaped(), userPhotoAlbum.getName(), String.valueOf( getPhotosQty() ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotosByAlbum( %d, %d );", photoAuthor.getId(), userPhotoAlbum.getId() );
			}
		};
	}

	@Override
	protected int getPhotosQty() {
		return services.getUserPhotoAlbumService().getUserPhotoAlbumPhotosQty( userPhotoAlbum.getId() );
	}
}
