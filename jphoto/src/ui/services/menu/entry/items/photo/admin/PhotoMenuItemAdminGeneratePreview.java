package ui.services.menu.entry.items.photo.admin;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

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
	public String getMenuCssClass() {
		return MENU_ITEM_ADMIN_CSS_CLASS;
	}

	@Override
	public boolean isAccessibleFor() {
		return isAccessorSuperAdmin() && menuEntry.getPhotoImageSourceType() == PhotosImportSource.FILE_SYSTEM;
	}

	@Override
	public String getCallbackMessage() {
		return translate( "PhotoMenuItem: The preview has been generated" );
	}
}
