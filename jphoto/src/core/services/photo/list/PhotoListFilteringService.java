package core.services.photo.list;

import core.general.user.User;
import core.services.photo.list.factory.AbstractPhotoFilteringStrategy;

public interface PhotoListFilteringService {

	AbstractPhotoFilteringStrategy galleryFilteringStrategy( User accessor );

	AbstractPhotoFilteringStrategy topBestFilteringStrategy();

	AbstractPhotoFilteringStrategy bestFilteringStrategy( User accessor );

	AbstractPhotoFilteringStrategy userCardFilteringStrategy( User user, User accessor );
}
