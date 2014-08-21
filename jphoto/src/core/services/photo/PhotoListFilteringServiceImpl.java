package core.services.photo;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.RestrictionService;
import core.services.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import ui.services.photo.listFactory.factory.AbstractPhotoFilteringStrategy;

import java.util.Date;

public class PhotoListFilteringServiceImpl implements PhotoListFilteringService {

	@Autowired
	private RestrictionService restrictionService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PhotoService photoService;

	@Override
	public AbstractPhotoFilteringStrategy galleryFilteringStrategy( final User accessor ) {

		return new AbstractPhotoFilteringStrategy() {

			@Override
			public boolean isPhotoHidden( final int photoId, final Date time ) {

				if ( isAccessorSuperAdmin( accessor ) ) {
					return false;
				}

				return restrictionService.isPhotoShowingInPhotoGalleryRestrictedOn( photoId, time );
			}
		};
	}

	@Override
	public AbstractPhotoFilteringStrategy topBestFilteringStrategy() {

		return new AbstractPhotoFilteringStrategy() {

			@Override
			public boolean isPhotoHidden( final int photoId, final Date time ) {
				return restrictionService.isPhotoShowingInTopBestRestrictedOn( photoId, time );
			}
		};
	}

	@Override
	public AbstractPhotoFilteringStrategy bestFilteringStrategy( final User accessor ) {

		return new AbstractPhotoFilteringStrategy() {

			@Override
			public boolean isPhotoHidden( final int photoId, final Date time ) {

				if ( isAccessorSuperAdmin( accessor ) ) {
					return false;
				}

				return restrictionService.isPhotoShowingInTopBestRestrictedOn( photoId, time );
			}
		};
	}

	@Override
	public AbstractPhotoFilteringStrategy userCardFilteringStrategy( final User user, final User accessor ) {

		return new AbstractPhotoFilteringStrategy() {

			@Override
			public boolean isPhotoHidden( final int photoId, final Date time ) { // TODO: change test for this

				if ( isAccessorSuperAdmin( accessor ) ) {
					return false;
				}

				final Photo photo = photoService.load( photoId );
				return securityService.userOwnThePhoto( user, photo ) && securityService.isPhotoWithingAnonymousPeriod( photo );
			}
		};
	}

	private  boolean isAccessorSuperAdmin( final User user ) {
		return securityService.isSuperAdminUser( user );
	}

	public void setRestrictionService( final RestrictionService restrictionService ) {
		this.restrictionService = restrictionService;
	}

	public void setSecurityService( final SecurityService securityService ) {
		this.securityService = securityService;
	}

	public void setPhotoService( final PhotoService photoService ) {
		this.photoService = photoService;
	}
}
