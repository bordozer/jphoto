package core.general.menus.photo.items;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import utils.TranslatorUtils;

public class PhotoMenuItemGoToAuthorPhotoByGenre extends AbstractPhotoMenuItem {

	public static final String BEAN_NAME = "photoMenuItemGoToAuthorPhotoByGenre";

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_GENRE;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int photoId, final User userWhoIsCallingMenu ) {
		final Genre genre = getGenre( photoId );
		final User photoAuthor = getPhotoAuthor( photoId );
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				final int photoQtyByUserAndGenre = photoService.getPhotoQtyByUserAndGenre( photoAuthor.getId(), genre.getId() );
				return TranslatorUtils.translate( "$1: photos in category '$2' ( $3 )"
					, photoAuthor.getNameEscaped(), genre.getName(), String.valueOf( photoQtyByUserAndGenre ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotosByGenre( %d, %d );", photoAuthor.getId(), genre.getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleForPhoto( final Photo photo, final User userWhoIsCallingMenu ) {
		if ( hideMenuItemBecauseEntryOfMenuCaller( photo, userWhoIsCallingMenu ) ) {
			return false;
		}

		return super.isAccessibleForPhoto( photo, userWhoIsCallingMenu ) && photoService.getPhotoQtyByUserAndGenre( photo.getUserId(), photo.getGenreId() ) > 1;
	}

	private Genre getGenre( final int photoId ) {
		return genreService.load( getPhoto( photoId ).getGenreId() );
	}
}
