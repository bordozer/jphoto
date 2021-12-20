package com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoImageLocationType;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

public class PhotoMenuItemAdminGeneratePreview extends AbstractPhotoMenuItem {

    public PhotoMenuItemAdminGeneratePreview(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.ADMIN_MENU_ITEM_GENERATE_PREVIEW;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
        return new AbstractEntryMenuItemCommand<Photo>(menuEntry, accessor, services) {
            @Override
            public String getMenuText() {
                return getTranslatorService().translate("PhotoMenuItem: Generate photo preview", getLanguage());
            }

            @Override
            public String getMenuCommand() {
                return String.format("generatePhotoPreview( %d, %s );", getId(), RELOAD_PHOTO_CALLBACK);
            }
        };
    }

    @Override
    public String getMenuCssClass() {
        return MENU_ITEM_ADMIN_CSS_CLASS;
    }

    @Override
    public boolean isAccessibleFor() {
        return isAccessorSuperAdmin() && menuEntry.getPhotoImageLocationType() == PhotoImageLocationType.FILE; // TODO: implement preview regeneration
    }

    @Override
    public String getCallbackMessage() {
        return translate("PhotoMenuItem: The preview has been generated");
    }
}
