package menuItems.user;

import common.AbstractTestCase;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import core.services.user.UserService;
import org.easymock.EasyMock;
import org.junit.Before;

public abstract class AbstractUserMenuItemTest_ extends AbstractTestCase {

	protected static final String WRONG_COMMAND = "Wrong menu text";

	protected static final String MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT = "Menu item should be accessible but it is not";
	protected static final String MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS = "Menu item should not be accessible but it is";

	protected UserMenuItemTestData testData;

	@Before
	public void setup() {
		super.setup();

		testData = new UserMenuItemTestData();
	}

	protected ServicesImpl getServices( final UserMenuItemTestData testData, final User accessor ) {
		final ServicesImpl services = new ServicesImpl();

		services.setUserService( getUserService() );
		services.setSecurityService( getSecurityService( accessor ) );

		return services;
	}

	private UserService getUserService() {
		final UserService userService = EasyMock.createMock( UserService.class );

		EasyMock.expect( userService.load( testData.getUser().getId() ) ).andReturn( testData.getUser() ).anyTimes();
		EasyMock.expect( userService.load( testData.getAccessor().getId() ) ).andReturn( testData.getAccessor() ).anyTimes();

		EasyMock.expect( userService.load( SUPER_MEGA_ADMIN.getId() ) ).andReturn( SUPER_MEGA_ADMIN ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		return userService;
	}

	private SecurityService getSecurityService( final User accessor ) {
		final boolean isAccessorAdmin = SUPER_ADMIN.getId() == accessor.getId() || SUPER_MEGA_ADMIN.getId() == accessor.getId();
		final boolean isUserAdmin = SUPER_ADMIN.getId() == testData.getUser().getId() || SUPER_MEGA_ADMIN.getId() == testData.getUser().getId();

		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.isSuperAdminUser( accessor.getId() ) ).andReturn( isAccessorAdmin ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( accessor ) ).andReturn( isAccessorAdmin ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( testData.getUser().getId() ) ).andReturn( isUserAdmin ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( testData.getUser() ) ).andReturn( isUserAdmin ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN.getId() ) ).andReturn( true ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN ) ).andReturn( true ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}
}
