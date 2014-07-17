package ui.services.menu.entry.items.photo.admin;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

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

		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, accessor, services ) {
			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "PhotoMenuItem: Lock photo author: $1", getLanguage(), photoAuthor.getNameEscaped() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "adminLockUser( %d, '%s' );", photoAuthor.getId(), photoAuthor.getNameEscaped() );
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
		return MENU_ITEM_ADMIN_CSS_CLASS;
	}
}