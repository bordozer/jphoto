package core.services.dao;

import core.general.user.User;
import core.general.user.UsersSecurity;

import java.util.Date;

public interface UsersSecurityDao {

	boolean createEntry( final int userId, final String cryptPassword );

	boolean save( final UsersSecurity usersSecurity );

	UsersSecurity load( final User user );

	void delete( final int userId );

	Date getLastUserActivityTime( final int userId );

	void saveLastUserActivityTime( final int userId, final Date time );

	void changeUserPassword( final int userId, final String cryptPassword );
}
