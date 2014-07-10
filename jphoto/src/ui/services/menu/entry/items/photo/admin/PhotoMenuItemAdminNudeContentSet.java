package ui.services.menu.entry.items.photo.admin;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

public class PhotoMenuItemAdminNudeContentSet extends AbstractPhotoMenuItem {

	public PhotoMenuItemAdminNudeContentSet( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_MENU_ITEM_NUDE_CONTENT_SET;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, accessor, services ) {
			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "PhotoMenuItem: Set nude content", getLanguage() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "adminPhotoNudeContentSet( %d, %s );", getId(), RELOAD_PHOTO_CALLBACK );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return isAccessorSuperAdmin() && ! menuEntry.isContainsNudeContent() && getGenreService().load( menuEntry.getGenreId() ).isCanContainNudeContent();
	}

	@Override
	public String getCallbackMessage() {
		return services.getTranslatorService().translate( "PhotoMenuItem: $1: Nude context has been set", getLanguage(), menuEntry.getNameEscaped() );
	}
}
