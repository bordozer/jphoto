package core.services.photo;

import core.general.user.User;
import ui.controllers.photos.list.factory.AbstractPhotoFilteringStrategy;

public interface PhotoListFilteringService {

	AbstractPhotoFilteringStrategy galleryFilteringStrategy( User accessor );

	AbstractPhotoFilteringStrategy topBestFilteringStrategy();

	AbstractPhotoFilteringStrategy bestFilteringStrategy( User accessor );

	AbstractPhotoFilteringStrategy userCardFilteringStrategy( User user, User accessor );
}
