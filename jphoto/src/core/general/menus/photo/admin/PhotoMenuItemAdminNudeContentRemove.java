package core.general.menus.photo.admin;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;

public class PhotoMenuItemAdminNudeContentRemove extends AbstractPhotoMenuItem {

	public PhotoMenuItemAdminNudeContentRemove( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_MENU_ITEM_NUDE_CONTENT_REMOVE;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, accessor, services ) {
			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "PhotoMenuItem: Remove nude content", getLanguage() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "adminPhotoNudeContentRemove( %d );", getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return isAccessorSuperAdmin() && menuEntry.isContainsNudeContent();
	}
}
