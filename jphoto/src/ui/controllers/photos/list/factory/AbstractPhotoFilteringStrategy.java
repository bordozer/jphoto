package ui.controllers.photos.list.factory;

import core.general.user.User;
import core.services.system.Services;

import java.util.Date;

public abstract class AbstractPhotoFilteringStrategy {

	public abstract boolean isPhotoHidden( final int photoId, final Date time );
}
