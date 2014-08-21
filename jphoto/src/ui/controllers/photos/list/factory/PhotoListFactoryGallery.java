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
}
