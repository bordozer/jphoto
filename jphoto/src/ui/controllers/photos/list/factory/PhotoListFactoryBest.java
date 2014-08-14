package ui.controllers.photos.list.factory;

import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;

public class PhotoListFactoryBest extends AbstractPhotoListFactory {

	@Override
	protected PhotoGroupOperationMenuContainer getPhotoGroupOperationMenuContainer() {
		return null;
	}

	@Override
	protected String getLinkToFullList() {
		return null;
	}

	@Override
	protected boolean showPaging() {
		return true;
	}

	public PhotoListFactoryBest( final User user, final Services services ) {
		super( user, services );
	}

	/*public PhotoListFactoryBest( final Services services ) {
		super( new PhotoListTitle( services.getPhotoListCriteriasService().getForAllPhotosTopBest( EnvironmentContext.getCurrentUser() ), services ), services );

		criterias = services.getPhotoListCriteriasService().getForAllPhotosTopBest( user );
		photoListTitle = new PhotoListTitleGallery( criterias, services );
	}

	public PhotoListFactoryBest( final Genre genre, final Services services ) {
		super( new PhotoListTitle( services.getPhotoListCriteriasService().getForAllPhotosTopBest( EnvironmentContext.getCurrentUser() ), services ), services );
		queryBuilder.filterByGenre( genre );
	}

	public PhotoListFactoryBest( final User user, final Services services ) {
		super( new PhotoListTitle( services.getPhotoListCriteriasService().getForAllPhotosTopBest( EnvironmentContext.getCurrentUser() ), services ), services );
		queryBuilder.filterByUser( user );
	}

	public PhotoListFactoryBest( final User user, final Genre genre, final Services services ) {
		super( new PhotoListTitle( services.getPhotoListCriteriasService().getForAllPhotosTopBest( EnvironmentContext.getCurrentUser() ), services ), services );
		queryBuilder.filterByUser( user ).filterByGenre( genre );
	}*/
}
