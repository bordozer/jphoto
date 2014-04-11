package ui.services.security;

import core.exceptions.NudeContentException;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import ui.context.EnvironmentContext;
import utils.UserUtils;

public class SecurityUIServiceImpl implements SecurityUIService {

	@Autowired
	private SecurityService securityService;

	@Override
	public void assertUserWantSeeNudeContent( final User user, final Photo photo, final String url ) {
		if ( isPhotoHasToBeHiddenBecauseOfNudeContent( photo, user ) ) {
			throw new NudeContentException( url );
		}
	}

	@Override
	public boolean isPhotoHasToBeHiddenBecauseOfNudeContent( final Photo photo, final User user ) {

		if ( securityService.isSuperAdminUser( user.getId() ) ) {
			return false;
		}

		final boolean isUserOwnerOfPhoto = securityService.userOwnThePhoto( user, photo );
		if ( ! photo.isContainsNudeContent() || isUserOwnerOfPhoto ) {
			return false;
		}

		final boolean userHasAlreadyConfirmedShowingNudeContent = EnvironmentContext.isShowNudeContext();
		if ( userHasAlreadyConfirmedShowingNudeContent ) {
			return false;
		}

		final boolean userDoesNotWantToSeeNudeContent = !user.isShowNudeContent();
		final boolean notLoggedUserHasNotConfirmedNudeContent = ! UserUtils.isLoggedUser( user );

		return userDoesNotWantToSeeNudeContent || notLoggedUserHasNotConfirmedNudeContent;
	}

	public void setSecurityService( final SecurityService securityService ) {
		this.securityService = securityService;
	}
}
