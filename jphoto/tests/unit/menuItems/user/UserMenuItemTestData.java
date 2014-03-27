package menuItems.user;

import common.AbstractTestCase;
import core.general.user.User;

public class UserMenuItemTestData {

	private final User user;
	private final User accessor;

	UserMenuItemTestData() {
		user = new User( 222 );
		user.setName( "Photo Author" );
		user.setLanguage( AbstractTestCase.MENU_LANGUAGE );

		accessor = new User( 333 );
		accessor.setName( "Just a User" );
		accessor.setLanguage( AbstractTestCase.MENU_LANGUAGE );
	}

	public User getUser() {
		return user;
	}

	public User getAccessor() {
		return accessor;
	}
}
