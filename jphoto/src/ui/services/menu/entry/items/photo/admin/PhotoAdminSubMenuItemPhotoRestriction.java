package ui.services.menu.entry.items.photo.admin;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;
import utils.StringUtilities;

public class PhotoAdminSubMenuItemPhotoRestriction extends AbstractPhotoMenuItem {

	public PhotoAdminSubMenuItemPhotoRestriction( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_PHOTO;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, accessor, services ) {
			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "PhotoAdminSubMenuItemPhotoRestriction: Photo restriction", getLanguage() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "adminRestrictPhoto( %d, '%s' );", menuEntry.getId(), StringUtilities.escapeJavaScript( menuEntry.getName() ) );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {

		if ( ! isAccessorSuperAdmin() ) {
			return false;
		}

		return true; //! isAccessorSeeingMenuOfOwnPhoto();
	}
}
