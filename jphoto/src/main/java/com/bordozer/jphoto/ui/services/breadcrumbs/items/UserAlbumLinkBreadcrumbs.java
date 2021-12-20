package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class UserAlbumLinkBreadcrumbs extends AbstractBreadcrumb {

    private UserPhotoAlbum photoAlbum;

    public UserAlbumLinkBreadcrumbs(final UserPhotoAlbum photoAlbum, final Services services) {
        super(services);
        this.photoAlbum = photoAlbum;
    }

    @Override
    public String getValue(final Language language) {
        return getEntityLinkUtilsService().getUserPhotoAlbumPhotosLink(photoAlbum, language);
    }
}
