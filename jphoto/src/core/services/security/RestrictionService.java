package core.services.security;

import core.general.user.User;

import java.util.Date;

public interface RestrictionService {

	void lockUser( final User user, final Date dateFrom, final Date dateTo );

}
