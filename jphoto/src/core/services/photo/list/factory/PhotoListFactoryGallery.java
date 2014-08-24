package core.services.photo.list.factory;

import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;
import core.services.utils.sql.PhotoListQueryBuilder;

public abstract class PhotoListFactoryGallery extends AbstractPhotoListFactory {

	public PhotoListFactoryGallery( final AbstractPhotoFilteringStrategy photoFilteringStrategy, final User accessor, final Services services ) {
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

	protected PhotoListQueryBuilder getTopBestBaseQuery( final int page, final int itemsOnPage ) {
		return new PhotoListQueryBuilder( services.getDateUtilsService() ).forPage( page, itemsOnPage ).sortByUploadTimeDesc();
	}
}
