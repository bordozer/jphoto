package ui.controllers.photos.list.factory;

import core.general.genre.Genre;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;
import ui.controllers.photos.list.title.PhotoListTitleGallery;
import utils.UserUtils;

import java.util.Date;

public class PhotoListFactoryGallery extends AbstractPhotoListFactory {

	private User user;

	public PhotoListFactoryGallery( final User accessor, final Services services ) {
		super( accessor, services );

		criterias = services.getPhotoListCriteriasService().getForAllPhotos( accessor );
		photoListTitle = getPhotoListTitle( services );
	}

	public PhotoListFactoryGallery( final Genre genre, final User accessor, final Services services ) {
		super( accessor, services );

		criterias = services.getPhotoListCriteriasService().getForGenre( genre, accessor );
		photoListTitle = getPhotoListTitle( services );
	}

	public PhotoListFactoryGallery( final User user, final User accessor, final Services services ) {
		super( accessor, services );

		criterias = services.getPhotoListCriteriasService().getForUser( user, accessor );
		photoListTitle = getPhotoListTitle( services );

		this.user = user;
	}

	public PhotoListFactoryGallery( final User user, final Genre genre, final User accessor, final Services services ) {
		super( accessor, services );

		criterias = services.getPhotoListCriteriasService().getForUserAndGenre( user, genre, accessor );
		photoListTitle = getPhotoListTitle( services );

		this.user = user;
	}

	@Override
	protected boolean isPhotoHidden( final int photoId, final Date currentTime ) {

		if ( user != null ) {
			return false;
		}

		if ( services.getSecurityService().isSuperAdminUser( accessor ) ) {
			return false;
		}

		if ( services.getSecurityService().userOwnThePhoto( accessor, photoId ) ) {
			return false; // TODO: should user see restricted photos in the gallery?
		}

		return services.getRestrictionService().isPhotoBeingInTopRestrictedOn( photoId, currentTime );
	}

	@Override
	protected PhotoGroupOperationMenuContainer getPhotoGroupOperationMenuContainer() {

		if ( UserUtils.isUsersEqual( user, accessor ) ) {
			return new PhotoGroupOperationMenuContainer( services.getGroupOperationService().getUserOwnPhotosGroupOperationMenus() );
		}

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

	private PhotoListTitleGallery getPhotoListTitle( final Services services ) {
		return new PhotoListTitleGallery( criterias, services );
	}
}
