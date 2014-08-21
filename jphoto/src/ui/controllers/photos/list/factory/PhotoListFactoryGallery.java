package ui.controllers.photos.list.factory;

import core.general.data.PhotoListCriterias;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;

public abstract class PhotoListFactoryGallery extends AbstractPhotoListFactory {

	public PhotoListFactoryGallery( final PhotoListCriterias criterias, final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services ) {
		super( criterias, photoFilteringStrategy, accessor, services );
	}

	@Override
	protected boolean showPaging() {
		return true;
	}

	@Override
	protected PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {
		return services.getGroupOperationService().getPhotoListPhotoGroupOperationMenuContainer( accessor );
	}

	/*public PhotoListFactoryGallery( final User accessor, final Services services ) {
		super( accessor, services );

		criterias = services.getPhotoListCriteriasService().getForAllPhotos( accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = services.getPhotoListFilteringService().galleryFilteringStrategy( accessor );
	}

	public PhotoListFactoryGallery( final Genre genre, final User accessor, final Services services ) {
		super( accessor, services );

		this.genre = genre;

		criterias = services.getPhotoListCriteriasService().getForGenre( genre, accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = services.getPhotoListFilteringService().galleryFilteringStrategy( accessor );
	}

	public PhotoListFactoryGallery( final User user, final User accessor, final Services services ) {
		super( accessor, services );

		this.user = user;

		criterias = services.getPhotoListCriteriasService().getForUser( user, accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = services.getPhotoListFilteringService().userCardFilteringStrategy( user, accessor );
	}

	public PhotoListFactoryGallery( final User user, final Genre genre, final User accessor, final Services services ) {
		super( accessor, services );

		this.user = user;
		this.genre = genre;

		criterias = services.getPhotoListCriteriasService().getForUserAndGenre( user, genre, accessor );
		photoListTitle = getPhotoListTitle( services );

		photoFilteringStrategy = services.getPhotoListFilteringService().userCardFilteringStrategy( user, accessor );
	}*/

	/*@Override
	protected PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {

		if ( isUserCard() && UserUtils.isUsersEqual( user, accessor ) ) {
			return new PhotoGroupOperationMenuContainer( services.getGroupOperationService().getUserOwnPhotosGroupOperationMenus() );
		}

		return services.getGroupOperationService().getPhotoListPhotoGroupOperationMenuContainer( accessor );
	}

	@Override
	protected boolean showPaging() {
		return true;
	}

	@Override
	protected String getPhotoListBottomText() {
		if ( genre != null ) {
			return genre.getDescription();
		}

		return super.getPhotoListBottomText();
	}

	private AbstractPhotoListTitle getPhotoListTitle( final Services services ) {

		if ( user != null || genre != null  ) {
			return new PhotoListTitle( criterias, services );
		}

		return new PhotoListTitleGallery( criterias, services );
	}*/
}
