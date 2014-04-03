package core.general.menus.photo.goTo;

import core.general.genre.Genre;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;

public class PhotoMenuItemGoToAuthorPhotoByGenre extends AbstractPhotoGoToAuthorPhotos {

	public PhotoMenuItemGoToAuthorPhotoByGenre( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_GENRE;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {

		final Genre genre = getGenre();
		final User photoAuthor = getPhotoAuthor();

		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, accessor, services ) {

			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "$1: photos in category '$2' ( $3 )", getLanguage(), photoAuthor.getNameEscaped(), getGenreNameTranslated( genre ), String.valueOf( getPhotosQty() ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotosByGenre( %d, %d );", photoAuthor.getId(), genre.getId() );
			}
		};
	}

	@Override
	protected int getPhotosQty() {
		return getPhotoService().getPhotoQtyByUserAndGenre( menuEntry.getUserId(), menuEntry.getGenreId() );
	}

	private Genre getGenre() {
		return getGenreService().load( menuEntry.getGenreId() );
	}
}
