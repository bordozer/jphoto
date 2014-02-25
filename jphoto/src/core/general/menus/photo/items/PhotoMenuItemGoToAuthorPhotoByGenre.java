package core.general.menus.photo.items;

import core.general.genre.Genre;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
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

		final Genre genre = getGenre();
		final User photoAuthor = getPhotoAuthor();

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
	public boolean isAccessibleFor() {

		if ( getPhotosInGenre() < 2 ) {
			return false;
		}

		if ( isSuperAdminUser( accessor ) ) {
			return true;
		}

		if ( hideMenuItemBecauseEntryOfMenuCaller() ) {
			return false;
		}

		if ( isPhotoIsWithinAnonymousPeriod() ) {
			return false;
		}

		return true;
	}

	private int getPhotosInGenre() {
		return getPhotoService().getPhotoQtyByUserAndGenre( menuEntry.getUserId(), menuEntry.getGenreId() );
	}

	private Genre getGenre() {
		return getGenreService().load( menuEntry.getGenreId() );
	}
}
