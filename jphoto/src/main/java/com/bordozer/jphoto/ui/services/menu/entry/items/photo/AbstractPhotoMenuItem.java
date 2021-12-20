package com.bordozer.jphoto.ui.services.menu.entry.items.photo;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItem;
import com.bordozer.jphoto.utils.UserUtils;

public abstract class AbstractPhotoMenuItem extends AbstractEntryMenuItem<Photo> {

    public static final String RELOAD_PHOTO_CALLBACK = "reloadPhotoCallback";
    public static final String DELETE_PHOTO_CALLBACK = "deletePhotoCallback";

    public AbstractPhotoMenuItem(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    protected User getPhotoAuthor() {
        return getUserService().load(menuEntry.getUserId());
    }

    protected boolean isPhotoIsWithinAnonymousPeriod() {
        return getSecurityService().isPhotoAuthorNameMustBeHidden(menuEntry, accessor);
    }

    protected boolean isPhotoOfMenuAccessor() {
        return UserUtils.isUsersEqual(accessor, getUserService().load(menuEntry.getUserId()));
    }

    protected boolean hideMenuItemBecauseEntryOfMenuCaller() {
        return isPhotoOfMenuAccessor() && isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOff();
    }

    protected GenreService getGenreService() {
        return services.getGenreService();
    }

    protected boolean isAccessorSeeingMenuOfOwnPhoto() {
        return UserUtils.isUsersEqual(accessor.getId(), menuEntry.getUserId());
    }

    protected boolean isAccessorSuperAdmin() {
        return getSecurityService().isSuperAdminUser(accessor.getId());
    }

    protected boolean isPhotoAuthorSuperAdmin() {
        return getSecurityService().isSuperAdminUser(menuEntry.getUserId());
    }

}
