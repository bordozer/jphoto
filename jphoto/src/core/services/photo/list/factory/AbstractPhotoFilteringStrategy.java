package core.services.photo.list.factory;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;

import java.util.Date;

public abstract class AbstractPhotoFilteringStrategy {

	protected final Services services;
	protected final User accessor;

	public abstract boolean isPhotoHidden( final int photoId, final Date time );

	protected AbstractPhotoFilteringStrategy( final User accessor, final Services services ) {
		this.services = services;
		this.accessor = accessor;
	}

	protected boolean isSuperAdmin( final User user ) {
		return services.getSecurityService().isSuperAdminUser( user );
	}

	protected boolean isPhotoAuthorInInvisibilityList( final int photoId ) {
		final Photo photo = services.getPhotoService().load( photoId );
		return services.getFavoritesService().isUserInMembersInvisibilityListOfUser( accessor.getId(), photo.getUserId() );
	}
}
