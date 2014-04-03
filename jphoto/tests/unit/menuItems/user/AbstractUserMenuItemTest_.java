package menuItems.user;

import common.AbstractTestCase;
import core.services.security.SecurityService;
import core.services.system.ServicesImpl;
import core.services.user.UserService;
import org.easymock.EasyMock;
import org.junit.Before;

public abstract class AbstractUserMenuItemTest_ extends AbstractTestCase {

	protected static final String MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT = "Menu item should be accessible but it is not";
	protected static final String MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS = "Menu item should not be accessible but it is";

	protected UserMenuItemTestData testData;

	@Before
	public void setup() {
		super.setup();

		testData = new UserMenuItemTestData();
	}

	@Override
	protected ServicesImpl getServices() {
		final ServicesImpl services = new ServicesImpl();

		services.setUserService( getUserService() );
		services.setSecurityService( getSecurityService() );
		services.setTranslatorService( translatorService );

		return services;
	}

	private UserService getUserService() {
		final UserService userService = EasyMock.createMock( UserService.class );

		EasyMock.expect( userService.load( testData.getUser().getId() ) ).andReturn( testData.getUser() ).anyTimes();
		EasyMock.expect( userService.load( testData.getAccessor().getId() ) ).andReturn( testData.getAccessor() ).anyTimes();

		EasyMock.expect( userService.load( SUPER_ADMIN_1.getId() ) ).andReturn( SUPER_ADMIN_1 ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		return userService;
	}

	private SecurityService getSecurityService() {

		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.isSuperAdminUser( NOT_LOGGED_USER.getId() ) ).andReturn( false ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( NOT_LOGGED_USER ) ).andReturn( false ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( testData.getUser().getId() ) ).andReturn( false ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( testData.getUser() ) ).andReturn( false ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( testData.getAccessor().getId() ) ).andReturn( false ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( testData.getAccessor() ) ).andReturn( false ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN_1.getId() ) ).andReturn( true ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN_1 ) ).andReturn( true ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN_2.getId() ) ).andReturn( true ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN_2 ) ).andReturn( true ).anyTimes();


		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}
}
