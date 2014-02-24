package menuItems.user;

import common.AbstractTestCase;
import core.services.security.ServicesImpl;
import core.services.user.UserService;
import org.easymock.EasyMock;
import org.junit.Before;

public class AbstractUserMenuItemTest_ extends AbstractTestCase {

	protected static final String WRONG_MENU_TEXT = "Wrong menu text";
	protected static final String MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT = "Menu item should be accessible but it is not";
	protected static final String MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS = "Menu item should not be accessible but it is";

	protected UserMenuItemTestData testData;

	@Before
	public void setup() {
		super.setup();

		testData = new UserMenuItemTestData();
	}

	protected ServicesImpl getServices( final UserMenuItemTestData testData ) {
		final ServicesImpl services = new ServicesImpl();

		services.setUserService( getUserService( testData ) );

		return services;
	}

	private UserService getUserService( final UserMenuItemTestData testData ) {
		final UserService userService = EasyMock.createMock( UserService.class );

		EasyMock.expect( userService.load( testData.getUser().getId() ) ).andReturn( testData.getUser() ).anyTimes();
		EasyMock.expect( userService.load( testData.getAccessor().getId() ) ).andReturn( testData.getAccessor() ).anyTimes();
		EasyMock.expect( userService.load( SUPER_MEGA_ADMIN.getId() ) ).andReturn( SUPER_MEGA_ADMIN ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		return userService;
	}
}
