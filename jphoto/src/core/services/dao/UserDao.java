package core.services.dao;

import core.general.user.User;
import core.general.user.UserStatus;

import java.util.List;

public interface UserDao extends BaseEntityDao<User> {

	List<User> loadAll();

	User loadByName( final String name );

	User loadByLogin( final String userLogin );

	User loadByEmail( final String userEmail );

	boolean setUserStatus( final int userId, final UserStatus userStatus );

	int getUserCount();

	List<User> searchByPartOfName( final String searchString );
}
