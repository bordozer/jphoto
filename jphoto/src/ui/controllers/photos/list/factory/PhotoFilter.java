package ui.controllers.photos.list.factory;

import core.general.genre.Genre;
import core.general.user.User;
import core.services.system.Services;

import java.util.Date;

public class PhotoFilter {

	private final User accessor;
	private final Services services;

	public PhotoFilter( final User accessor, final Services services ) {
		this.accessor = accessor;
		this.services = services;
	}

	public AbstractPhotoFilteringStrategy galleryFilteringStrategy() {

		return new AbstractPhotoFilteringStrategy( accessor, services ) {

			@Override
			public boolean isPhotoHidden( final Integer photoId, final Date time ) {

				if ( isAccessorSuperAdmin() ) {
					return false;
				}

				return services.getRestrictionService().isPhotoShowingInPhotoGalleryRestrictedOn( photoId, time );
			}
		};
	}

	public AbstractPhotoFilteringStrategy topBestFilteringStrategy() {

		return new AbstractPhotoFilteringStrategy( accessor, services ) {

			@Override
			public boolean isPhotoHidden( final Integer photoId, final Date time ) {
				return services.getRestrictionService().isPhotoShowingInTopBestRestrictedOn( photoId, time );
			}
		};
	}

	public AbstractPhotoFilteringStrategy bestFilteringStrategy() {

		return new AbstractPhotoFilteringStrategy( accessor, services ) {

			@Override
			public boolean isPhotoHidden( final Integer photoId, final Date time ) {

				if ( isAccessorSuperAdmin() ) {
					return false;
				}

				return services.getRestrictionService().isPhotoShowingInTopBestRestrictedOn( photoId, time );
			}
		};
	}

	public AbstractPhotoFilteringStrategy userCardFilteringStrategy( final User user ) {

		return new AbstractPhotoFilteringStrategy( accessor, services ) {

			@Override
			public boolean isPhotoHidden( final Integer photoId, final Date time ) {
				return false;
			}
		};
	}

	private  boolean isAccessorSuperAdmin() {
		return services.getSecurityService().isSuperAdminUser( accessor );
	}
}
