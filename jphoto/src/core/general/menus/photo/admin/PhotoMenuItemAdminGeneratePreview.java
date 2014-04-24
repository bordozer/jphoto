package core.general.menus.photo.admin;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;

public class PhotoMenuItemAdminGeneratePreview extends AbstractPhotoMenuItem {

	public PhotoMenuItemAdminGeneratePreview( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_MENU_ITEM_GENERATE_PREVIEW;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, accessor, services ) {
			@Override
			public String getMenuText() {
				return getTranslatorService().translate( "PhotoMenuItem: Generate photo preview", getLanguage() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "generatePhotoPreview( %d, %s );", getId(), RELOAD_PHOTO_CALLBACK );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return isAccessorSuperAdmin();
	}

	@Override
	public String getCallbackMessage() {
		return translate( "PhotoMenuItem: The preview has been generated" );
	}
}
