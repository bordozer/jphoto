package core.services.photo.list.factory;

import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;

public abstract class PhotoListFactoryBest extends AbstractPhotoListFactory {

	public PhotoListFactoryBest( final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services ) {
		super( photoFilteringStrategy, accessor, services );
	}

	@Override
	public boolean showPaging() {
		return true;
	}

	@Override
	public PhotoGroupOperationMenuContainer getGroupOperationMenuContainer() {
		return services.getGroupOperationService().getPhotoListPhotoGroupOperationMenuContainer( accessor );
	}
}
