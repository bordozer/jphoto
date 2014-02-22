package core.general.menus.photo.items;

import core.general.photo.Photo;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.services.security.Services;
import utils.TranslatorUtils;

public class PhotoMenuItemGoToAuthorPhotos extends AbstractPhotoMenuItem {

	public PhotoMenuItemGoToAuthorPhotos( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.GO_TO_USER_PHOTOS;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {

		final User photoAuthor = getPhotoAuthor( menuEntry );

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
	public boolean isAccessibleFor( final Photo photo, final User userWhoIsCallingMenu ) {
		if ( hideMenuItemBecauseEntryOfMenuCaller( photo, userWhoIsCallingMenu ) ) {
			return false;
		}

		return super.isAccessibleFor( photo, userWhoIsCallingMenu ) && getPhotoQtyByUser( photo.getUserId() ) > 1;
	}

	private int getPhotoQtyByUser( final int userId ) {
		return getPhotoService().getPhotoQtyByUser( userId );
	}
}
