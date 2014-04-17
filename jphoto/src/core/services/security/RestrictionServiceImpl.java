package core.services.security;

import core.enums.RestrictionType;
import core.general.photo.Photo;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.services.dao.RestrictionDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class RestrictionServiceImpl implements RestrictionService {

	@Autowired
	private RestrictionDao restrictionDao;

	@Override
	public void lockUser( final User user, final Date timeFrom, final Date timeTo ) {
		final EntryRestriction<User> restriction = new EntryRestriction<>( user, RestrictionType.USER_LOGIN );
		restriction.setRestrictionTimeFrom( timeFrom );
		restriction.setRestrictionTimeTo( timeTo );

		restrictionDao.saveToDB( restriction );
	}

	@Override
	public void lockPhotoToBePhotoOfTheDay( final Photo photo, final Date timeFrom, final Date timeTo ) {
		final EntryRestriction<Photo> restriction = new EntryRestriction<>( photo, RestrictionType.PHOTO_BE_PHOTO_OF_THE_DAY );
		restriction.setRestrictionTimeFrom( timeFrom );
		restriction.setRestrictionTimeTo( timeTo );

		restrictionDao.saveToDB( restriction );
	}
}
