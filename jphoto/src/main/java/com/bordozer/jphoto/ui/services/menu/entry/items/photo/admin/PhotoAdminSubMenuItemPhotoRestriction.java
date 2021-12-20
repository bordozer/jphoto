package com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;
import com.bordozer.jphoto.utils.StringUtilities;

public class PhotoAdminSubMenuItemPhotoRestriction extends AbstractPhotoMenuItem {

    public PhotoAdminSubMenuItemPhotoRestriction(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_PHOTO;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
        return new AbstractEntryMenuItemCommand<Photo>(menuEntry, accessor, services) {
            @Override
            public String getMenuText() {
                return getTranslatorService().translate("PhotoAdminSubMenuItemPhotoRestriction: Photo restriction", getLanguage());
            }

            @Override
            public String getMenuCommand() {
                return String.format("adminRestrictPhoto( %d, '%s' );", menuEntry.getId(), StringUtilities.escapeJavaScript(menuEntry.getName()));
            }
        };
    }

    @Override
    public boolean isAccessibleFor() {

        if (!isAccessorSuperAdmin()) {
            return false;
        }

        return true; //! isAccessorSeeingMenuOfOwnPhoto();
    }
}
