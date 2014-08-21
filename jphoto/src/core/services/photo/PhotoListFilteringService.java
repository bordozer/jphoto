package core.services.photo;

import core.general.user.User;
import ui.services.photo.listFactory.factory.AbstractPhotoFilteringStrategy;

public interface PhotoListFilteringService {

	AbstractPhotoFilteringStrategy galleryFilteringStrategy( User accessor );

	AbstractPhotoFilteringStrategy topBestFilteringStrategy();

	AbstractPhotoFilteringStrategy bestFilteringStrategy( User accessor );

	AbstractPhotoFilteringStrategy userCardFilteringStrategy( User user, User accessor );
}
