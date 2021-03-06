package com.bordozer.jphoto.ui.services.security;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;

public interface SecurityUIService {

    void assertUserWantSeeNudeContent(final User user, final Photo photo, final String url);

    boolean isPhotoHasToBeHiddenBecauseOfNudeContent(final Photo photo, final User user);
}
