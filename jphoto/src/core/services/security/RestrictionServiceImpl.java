package core.services.security;

import core.enums.RestrictionType;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.services.dao.RestrictionDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class RestrictionServiceImpl implements RestrictionService {

	@Autowired
	private RestrictionDao restrictionDao;

	@Override
	public void lockUser( final User user, final Date dateFrom, final Date dateTo ) {
		final EntryRestriction restriction = new EntryRestriction( user.getId(), RestrictionType.USER_LOGIN );
		restrictionDao.saveToDB( restriction );
	}
}
