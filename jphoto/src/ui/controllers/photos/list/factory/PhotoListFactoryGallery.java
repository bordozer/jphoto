package ui.controllers.photos.list.factory;

import core.general.genre.Genre;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;
import ui.controllers.photos.list.title.PhotoListTitleGallery;

import java.util.Date;

public class PhotoListFactoryGallery extends AbstractPhotoListFactory {

	public PhotoListFactoryGallery( final User user, final Services services ) {
		super( user, services );

		criterias = services.getPhotoListCriteriasService().getForAllPhotos( user );
		photoListTitle = new PhotoListTitleGallery( criterias, services );
	}

	public PhotoListFactoryGallery( final Genre genre, final User user, final Services services ) {
		super( user, services );

		criterias = services.getPhotoListCriteriasService().getForGenre( genre, user );
		photoListTitle = new PhotoListTitleGallery( criterias, services );
	}

	@Override
	protected boolean isPhotoHidden( final int photoId, final Date currentTime ) {
		return services.getRestrictionService().isPhotoBeingInTopRestrictedOn( photoId, currentTime )
			&& ! services.getSecurityService().isSuperAdminUser( accessor )
			;
//		&& ( services.getSecurityService().userOwnThePhoto( accessor, photoId ) || services.getSecurityService().isSuperAdminUser( accessor ) )
	}

	@Override
	protected PhotoGroupOperationMenuContainer getPhotoGroupOperationMenuContainer() {
		return services.getGroupOperationService().getPhotoListPhotoGroupOperationMenuContainer( accessor );
	}

	@Override
	protected String getLinkToFullList() {
		return null;
	}

	@Override
	protected boolean showPaging() {
		return true;
	}
}
