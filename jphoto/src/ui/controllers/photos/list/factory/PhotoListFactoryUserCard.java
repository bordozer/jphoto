package ui.controllers.photos.list.factory;

import core.general.photo.group.PhotoGroupOperationMenuContainer;
import core.general.user.User;
import core.services.system.Services;

import java.util.Date;

public class PhotoListFactoryUserCard extends AbstractPhotoListFactory {

	public PhotoListFactoryUserCard( final User accessor, final Services services ) {
		super( accessor, services );
	}

	@Override
	protected String getLinkToFullList() {
		return null;
	}

	@Override
	protected boolean showPaging() {
		return false;
	}

	@Override
	protected boolean isPhotoHidden( final int photoId, final Date currentTime ) {
		return false;
	}

	@Override
	protected PhotoGroupOperationMenuContainer getPhotoGroupOperationMenuContainer() {
		return null;
	}
}
