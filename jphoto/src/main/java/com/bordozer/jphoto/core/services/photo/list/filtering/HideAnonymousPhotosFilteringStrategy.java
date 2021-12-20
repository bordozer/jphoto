package com.bordozer.jphoto.core.services.photo.list.filtering;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.photo.list.factory.AbstractPhotoFilteringStrategy;
import com.bordozer.jphoto.core.services.system.Services;

import java.util.Date;

public class HideAnonymousPhotosFilteringStrategy extends AbstractPhotoFilteringStrategy {

    public HideAnonymousPhotosFilteringStrategy(final User accessor, final Services services) {
        super(accessor, services);
    }

    @Override
    public boolean isPhotoHidden(final int photoId, final Date time) {
        final Photo photo = services.getPhotoService().load(photoId);
        return services.getSecurityService().isPhotoAuthorNameMustBeHidden(photo, accessor);
    }
}
