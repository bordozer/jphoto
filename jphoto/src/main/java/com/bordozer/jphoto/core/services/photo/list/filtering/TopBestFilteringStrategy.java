package com.bordozer.jphoto.core.services.photo.list.filtering;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.photo.list.factory.AbstractPhotoFilteringStrategy;
import com.bordozer.jphoto.core.services.system.Services;

import java.util.Date;

public class TopBestFilteringStrategy extends AbstractPhotoFilteringStrategy {

    public TopBestFilteringStrategy(final User accessor, final Services services) {
        super(accessor, services);
    }

    @Override
    public boolean isPhotoHidden(final int photoId, final Date time) {

        if (isPhotoAuthorInInvisibilityList(photoId)) {
            return true;
        }

        return services.getRestrictionService().isPhotoShowingInTopBestRestrictedOn(photoId, time);
    }
}
