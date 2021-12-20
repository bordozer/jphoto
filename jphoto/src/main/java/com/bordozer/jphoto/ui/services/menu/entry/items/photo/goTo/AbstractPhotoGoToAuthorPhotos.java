package com.bordozer.jphoto.ui.services.menu.entry.items.photo.goTo;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

public abstract class AbstractPhotoGoToAuthorPhotos extends AbstractPhotoMenuItem {

    protected abstract int getPhotosQty();

    public AbstractPhotoGoToAuthorPhotos(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public boolean isAccessibleFor() {

        if (getPhotosQty() < 2) {
            return false;
        }

        if (isSuperAdminUser(accessor)) {
            return true;
        }

        if (hideMenuItemBecauseEntryOfMenuCaller()) {
            return false;
        }

        if (isPhotoIsWithinAnonymousPeriod()) {
            return false;
        }

        return true;
    }
}
