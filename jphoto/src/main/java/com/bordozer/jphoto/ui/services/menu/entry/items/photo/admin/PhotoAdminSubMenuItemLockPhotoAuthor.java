package com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

public class PhotoAdminSubMenuItemLockPhotoAuthor extends AbstractPhotoMenuItem {

    public PhotoAdminSubMenuItemLockPhotoAuthor(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {

        final User photoAuthor = getPhotoAuthor();

        return new AbstractEntryMenuItemCommand<Photo>(menuEntry, accessor, services) {
            @Override
            public String getMenuText() {
                return getTranslatorService().translate("PhotoMenuItem: Restrict photo author: $1", getLanguage(), photoAuthor.getNameEscaped());
            }

            @Override
            public String getMenuCommand() {
                return String.format("adminRestrictUser( %d, '%s' );", photoAuthor.getId(), photoAuthor.getNameEscaped());
            }
        };
    }

    @Override
    public boolean isAccessibleFor() {

        if (isPhotoAuthorSuperAdmin()) {
            return false;
        }

        if (!isAccessorSuperAdmin()) {
            return false;
        }

        return !isAccessorSeeingMenuOfOwnPhoto();
    }

    @Override
    public String getMenuCssClass() {
        return MENU_ITEM_ADMIN_CSS_CLASS;
    }
}
