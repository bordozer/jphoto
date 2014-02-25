package core.general.menus.photo.items;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
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

		final User photoAuthor = getPhotoAuthor();

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				final int photoQtyByUser = getPhotoQtyByUser();
				return TranslatorUtils.translate( "$1: all photos ( $2 )", photoAuthor.getNameEscaped(), String.valueOf( photoQtyByUser ) );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "goToMemberPhotos( %d );", photoAuthor.getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {

		if ( getPhotoQtyByUser() < 2 ) {
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

	private int getPhotoQtyByUser() {
		return getPhotoService().getPhotoQtyByUser( menuEntry.getUserId() );
	}
}
