package ui.controllers.photos.list.factory;

import java.util.Date;

public abstract class AbstractPhotoFilteringStrategy {

	public abstract boolean isPhotoHidden( final int photoId, final Date time );
}
