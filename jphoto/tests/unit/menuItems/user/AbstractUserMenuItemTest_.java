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

	protected ServicesImpl getServices() {
		final ServicesImpl services = new ServicesImpl();

		services.setUserService( getUserService() );
		services.setSecurityService( getSecurityService() );

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

	private SecurityService getSecurityService() {

		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		final boolean isUserAdmin = SUPER_ADMIN.getId() == testData.getUser().getId() || SUPER_MEGA_ADMIN.getId() == testData.getUser().getId();
		EasyMock.expect( securityService.isSuperAdminUser( testData.getUser().getId() ) ).andReturn( isUserAdmin ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( testData.getUser() ) ).andReturn( isUserAdmin ).anyTimes();

		final boolean isAccessorAdmin = SUPER_ADMIN.getId() == testData.getAccessor().getId() || SUPER_MEGA_ADMIN.getId() == testData.getAccessor().getId();
		EasyMock.expect( securityService.isSuperAdminUser( testData.getAccessor().getId() ) ).andReturn( isUserAdmin ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( testData.getAccessor() ) ).andReturn( isAccessorAdmin ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( SUPER_MEGA_ADMIN.getId() ) ).andReturn( true ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( SUPER_MEGA_ADMIN ) ).andReturn( true ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN.getId() ) ).andReturn( true ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN ) ).andReturn( true ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( User.NOT_LOGGED_USER.getId() ) ).andReturn( false ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( User.NOT_LOGGED_USER ) ).andReturn( false ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}
}
