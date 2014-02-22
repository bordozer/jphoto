package core.general.menus.photo.items;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.services.security.Services;
import utils.TranslatorUtils;

public class PhotoMenuItemGoToAuthorPhotoByGenre extends AbstractPhotoMenuItem {

	public PhotoMenuItemGoToAuthorPhotoByGenre( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_GENRE;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {

		final int photoId = menuEntry.getId();
		final Genre genre = getGenre( photoId );
		final User photoAuthor = getPhotoAuthor( menuEntry );

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				final int photoQtyByUserAndGenre = getPhotoService().getPhotoQtyByUserAndGenre( photoAuthor.getId(), genre.getId() );
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
	public boolean isAccessibleFor( final Photo photo, final User userWhoIsCallingMenu ) {
		if ( hideMenuItemBecauseEntryOfMenuCaller( photo, userWhoIsCallingMenu ) ) {
			return false;
		}

		return super.isAccessibleFor( photo, userWhoIsCallingMenu ) && getPhotoService().getPhotoQtyByUserAndGenre( photo.getUserId(), photo.getGenreId() ) > 1;
	}

	private Genre getGenre( final int photoId ) {
		return getGenreService().load( getPhoto( photoId ).getGenreId() );
	}
}
