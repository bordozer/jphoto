package ui.services.security;

import core.general.photo.Photo;
import core.general.user.User;

public interface SecurityUIService {

	void assertUserWantSeeNudeContent( final User user, final Photo photo, final String url );

	boolean isPhotoHasToBeHiddenBecauseOfNudeContent( final Photo photo, final User user );
}
