package core.services.photo.list.filtering;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.photo.list.factory.AbstractPhotoFilteringStrategy;
import core.services.security.SecurityService;
import core.services.system.Services;

import java.util.Date;

public class UserCardFilteringStrategy extends AbstractPhotoFilteringStrategy {

	private final User user;

	public UserCardFilteringStrategy( final User user, final User accessor, final Services services ) {
		super( accessor, services );
		this.user = user;
	}

	@Override
	public boolean isPhotoHidden( final int photoId, final Date time ) {

		if ( isSuperAdmin( accessor ) ) {
			return false;
		}

		final SecurityService securityService = services.getSecurityService();

		final Photo photo = services.getPhotoService().load( photoId );
		return ! securityService.userOwnThePhoto( accessor, photo ) && securityService.isPhotoWithingAnonymousPeriod( photo );
	}
}
