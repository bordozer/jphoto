package ui.services.menu.entry.items.photo.admin;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

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
				return String.format( "adminPhotoNudeContentRemove( %d, %s );", getId(), RELOAD_PHOTO_CALLBACK );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return isAccessorSuperAdmin() && menuEntry.isContainsNudeContent();
	}

	@Override
	public String getCallbackMessage() {
		return services.getTranslatorService().translate( "PhotoMenuItem: $1: Nude context has been removed", getLanguage(), menuEntry.getNameEscaped() );
	}
}
