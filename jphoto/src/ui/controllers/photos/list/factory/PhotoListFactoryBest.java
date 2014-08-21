package ui.controllers.photos.list.factory;

import core.general.data.PhotoListCriterias;
import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;

public abstract class PhotoListFactoryBest extends AbstractPhotoListFactory {

	public PhotoListFactoryBest( final PhotoListCriterias criterias, final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services ) {
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
