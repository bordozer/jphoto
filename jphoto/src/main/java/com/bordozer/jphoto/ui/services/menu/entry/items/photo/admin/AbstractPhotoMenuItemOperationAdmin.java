package com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

public abstract class AbstractPhotoMenuItemOperationAdmin extends AbstractPhotoMenuItem {

    public AbstractPhotoMenuItemOperationAdmin(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    protected abstract boolean isSystemConfigurationKeyIsOn();

    @Override
    public boolean isAccessibleFor() {

        if (isAccessorSeeingMenuOfOwnPhoto()) {
            return false;
        }

        return isAccessorSuperAdmin() && isSystemConfigurationKeyIsOn();
    }
}
