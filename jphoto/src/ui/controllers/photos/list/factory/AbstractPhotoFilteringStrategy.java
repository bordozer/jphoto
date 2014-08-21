package ui.controllers.photos.list.factory;

import core.general.user.User;
import core.services.system.Services;

import java.util.Date;

public abstract class AbstractPhotoFilteringStrategy {

	protected final User accessor;
	protected final Services services;

	public abstract boolean isPhotoHidden( final Integer photoId, final Date time );

	protected AbstractPhotoFilteringStrategy( final User accessor, final Services services ) {
		this.accessor = accessor;
		this.services = services;
	}
}
