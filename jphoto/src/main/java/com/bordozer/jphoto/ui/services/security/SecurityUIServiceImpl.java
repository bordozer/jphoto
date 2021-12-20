package com.bordozer.jphoto.ui.services.security;

import com.bordozer.jphoto.core.exceptions.NudeContentException;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("securityUIService")
public class SecurityUIServiceImpl implements SecurityUIService {

    @Autowired
    private SecurityService securityService;

    @Override
    public void assertUserWantSeeNudeContent(final User user, final Photo photo, final String url) {
        if (isPhotoHasToBeHiddenBecauseOfNudeContent(photo, user)) {
            throw new NudeContentException(url);
        }
    }

    @Override
    public boolean isPhotoHasToBeHiddenBecauseOfNudeContent(final Photo photo, final User user) {

        if (securityService.isSuperAdminUser(user.getId())) {
            return false;
        }

        final boolean isUserOwnerOfPhoto = securityService.userOwnThePhoto(user, photo);
        if (!photo.isContainsNudeContent() || isUserOwnerOfPhoto) {
            return false;
        }

        final boolean userHasAlreadyConfirmedShowingNudeContent = EnvironmentContext.isShowNudeContext();
        if (userHasAlreadyConfirmedShowingNudeContent) {
            return false;
        }

        final boolean userDoesNotWantToSeeNudeContent = !user.isShowNudeContent();
        final boolean notLoggedUserHasNotConfirmedNudeContent = !UserUtils.isLoggedUser(user);

        return userDoesNotWantToSeeNudeContent || notLoggedUserHasNotConfirmedNudeContent;
    }

    public void setSecurityService(final SecurityService securityService) {
        this.securityService = securityService;
    }
}
