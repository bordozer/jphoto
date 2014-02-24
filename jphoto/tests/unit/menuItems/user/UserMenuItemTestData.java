package menuItems.user;

import core.general.user.User;

class UserMenuItemTestData {

	private final User user;
	private final User accessor;

	UserMenuItemTestData() {
		user = new User( 222 );
		user.setName( "Photo Author" );

		accessor = new User( 333 );
		accessor.setName( "Just a User" );
	}

	public User getUser() {
		return user;
	}

	public User getAccessor() {
		return accessor;
	}
}
