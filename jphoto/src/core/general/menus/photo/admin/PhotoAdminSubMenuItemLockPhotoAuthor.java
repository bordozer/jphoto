package core.general.menus.photo.admin;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;

public class PhotoAdminSubMenuItemLockPhotoAuthor extends AbstractPhotoMenuItem {

	public PhotoAdminSubMenuItemLockPhotoAuthor( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {

		final User photoAuthor = getPhotoAuthor();

		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, services ) {
			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "Lock photo author: $1", photoAuthor.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "adminLockUser( %d, '%s' ); return false;", photoAuthor.getId(), photoAuthor.getNameEscaped() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {

		if ( isPhotoAuthorSuperAdmin() ) {
			return false;
		}

		if ( ! isAccessorSuperAdmin() ) {
			return false;
		}

		return ! isAccessorSeeingMenuOfOwnPhoto();
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_CSS_CLASS_ADMIN;
	}
}
