package utils;

import core.context.EnvironmentContext;
import core.general.photo.Photo;
import core.general.user.User;

public class UserUtils {

	public static boolean isUsersEqual( final int user1Id, final int user2Id ) {
		return user1Id == user2Id;
	}

	public static boolean isUsersEqual( final User user1, final User user2 ) {
		return isUsersEqual( user1.getId(), user2.getId() );
	}

	public static boolean isLoggedUser( final int userId ) {
		return userId > 0 && ! isUsersEqual( userId, User.NOT_LOGGED_USER.getId() );
	}

	public static boolean isLoggedUser( final User user ) {
		return isLoggedUser( user.getId() );
	}

	public static boolean isCurrentUserLoggedUser() {
		return isLoggedUser( EnvironmentContext.getCurrentUser() );
	}

	public static boolean isUserEqualsToCurrentUser( final User user ) {
		return isUserEqualsToCurrentUser( user.getId() );
	}

	public static boolean isUserEqualsToCurrentUser( final int userId ) {
		return isLoggedUser( userId ) && isUsersEqual( EnvironmentContext.getCurrentUser().getId(), userId );
	}

	public static boolean isUserOwnThePhoto( final User user, final Photo photo ) {
		return photo.getUserId() == user.getId();
	}
}
