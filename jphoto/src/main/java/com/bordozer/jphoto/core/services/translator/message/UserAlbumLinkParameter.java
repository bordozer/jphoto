package com.bordozer.jphoto.core.services.translator.message;

import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class UserAlbumLinkParameter extends AbstractTranslatableMessageParameter {

    private UserPhotoAlbum userPhotoAlbum;

    public UserAlbumLinkParameter(final UserPhotoAlbum userPhotoAlbum, final Services services) {
        super(services);
        this.userPhotoAlbum = userPhotoAlbum;
    }

    @Override
    public String getValue(final Language language) {
        return services.getEntityLinkUtilsService().getUserPhotoAlbumPhotosLink(userPhotoAlbum, language);
    }
}
