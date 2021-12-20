package com.bordozer.jphoto.ui.services.menu.entry.items.comment.goTo;

import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.AbstractCommentMenuItem;

public abstract class AbstractCommentGoToAuthorPhotos extends AbstractCommentMenuItem {

    public AbstractCommentGoToAuthorPhotos(final PhotoComment photoComment, final User accessor, final Services services) {
        super(photoComment, accessor, services);
    }

    public abstract int getPhotoQty();

    @Override
    public boolean isAccessibleFor() {
        if (hideMenuItemBecauseEntryOfMenuCaller()) {
            return false;
        }

        if (isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod()) {
            return false;
        }

        return getPhotoQty() > minPhotosForMenu();
    }
}
