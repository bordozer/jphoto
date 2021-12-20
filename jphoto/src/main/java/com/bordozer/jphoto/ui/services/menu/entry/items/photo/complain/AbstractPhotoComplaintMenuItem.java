package com.bordozer.jphoto.ui.services.menu.entry.items.photo.complain;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.ComplaintReasonType;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

public abstract class AbstractPhotoComplaintMenuItem extends AbstractPhotoMenuItem {

    protected abstract ComplaintReasonType getComplainReasonType();

    protected abstract String getMenuItemText();

    public AbstractPhotoComplaintMenuItem(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public boolean isAccessibleFor() {

        if (!isMenuAccessorLogged()) {
            return false;
        }

        if (isAccessorSuperAdmin()) {
            return false;
        }

        if (isPhotoOfMenuAccessor()) {
            return false;
        }

        return true;
    }
}
