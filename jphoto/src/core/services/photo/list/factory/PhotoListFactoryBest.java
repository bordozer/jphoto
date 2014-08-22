package core.services.photo.list.factory;

import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;
import sql.builder.SqlIdsSelectQuery;

public abstract class PhotoListFactoryBest extends AbstractPhotoListFactory {

	public PhotoListFactoryBest( final SqlIdsSelectQuery selectIdsQuery, final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services ) {
		super( selectIdsQuery, photoFilteringStrategy, accessor, services );
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
