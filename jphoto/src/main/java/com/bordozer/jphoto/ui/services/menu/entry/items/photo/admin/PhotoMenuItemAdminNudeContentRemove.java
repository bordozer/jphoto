package com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

public class PhotoMenuItemAdminNudeContentRemove extends AbstractPhotoMenuItem {

    public PhotoMenuItemAdminNudeContentRemove(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.ADMIN_MENU_ITEM_NUDE_CONTENT_REMOVE;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
        return new AbstractEntryMenuItemCommand<Photo>(menuEntry, accessor, services) {
            @Override
            public String getMenuText() {
                return getTranslatorService().translate("PhotoMenuItem: Remove nude content", getLanguage());
            }

            @Override
            public String getMenuCommand() {
                return String.format("adminPhotoNudeContentRemove( %d, %s );", getId(), RELOAD_PHOTO_CALLBACK);
            }
        };
    }

    @Override
    public boolean isAccessibleFor() {
        return isAccessorSuperAdmin() && menuEntry.isContainsNudeContent();
    }

    @Override
    public String getCallbackMessage() {
        return services.getTranslatorService().translate("PhotoMenuItem: $1: Nude context has been removed", getLanguage(), menuEntry.getNameEscaped());
    }
}
