package core.services.security;

import core.general.photo.Photo;
import core.general.user.User;

import java.util.Date;

public interface RestrictionService {

	void lockUser( final User user, final Date timeFrom, final Date timeTo );

	void lockPhotoToBePhotoOfTheDay( final Photo photo, final Date timeFrom, final Date timeTo );
}
