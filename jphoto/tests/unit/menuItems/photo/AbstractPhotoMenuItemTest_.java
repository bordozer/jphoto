package menuItems.photo;

import common.AbstractTestCase;
import core.general.user.User;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import core.services.user.UserService;
import org.easymock.EasyMock;
import org.junit.Before;

public abstract class AbstractPhotoMenuItemTest_ extends AbstractTestCase {

	protected static final String MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT = "Menu item should be accessible but it is not";
	protected static final String MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS = "Menu item should not be accessible but it is";

	protected PhotoMenuItemTestData testData;

	@Before
	public void setup() {
		super.setup();

		testData = new PhotoMenuItemTestData();
	}

	protected ServicesImpl getServices( final User accessor ) {
		final ServicesImpl services = new ServicesImpl();

		services.setPhotoService( getPhotoService() );
		services.setSecurityService( getSecurityService( accessor ) );
		services.setUserService( getUserService() );
		services.setTranslatorService( translatorService );

		return services;
	}

	private UserService getUserService() {
		final UserService userService = EasyMock.createMock( UserService.class );

		EasyMock.expect( userService.load( testData.getAccessor().getId() ) ).andReturn( testData.getAccessor() ).anyTimes();
		EasyMock.expect( userService.load( testData.getPhotoAuthor().getId() ) ).andReturn( testData.getPhotoAuthor() ).anyTimes();
		EasyMock.expect( userService.load( SUPER_ADMIN_1.getId() ) ).andReturn( SUPER_ADMIN_1 ).anyTimes();
		EasyMock.expect( userService.load( SUPER_ADMIN_2.getId() ) ).andReturn( SUPER_ADMIN_2 ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		return userService;
	}

	private SecurityService getSecurityService( final User accessor ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.userOwnThePhoto( accessor, testData.getPhoto().getId() ) ).andReturn( testData.getPhoto().getUserId() == accessor.getId() ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( User.NOT_LOGGED_USER.getId() ) ).andReturn( false ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( User.NOT_LOGGED_USER ) ).andReturn( false ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( testData.getAccessor().getId() ) ).andReturn( false ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( testData.getAccessor() ) ).andReturn( false ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( testData.getPhotoAuthor().getId() ) ).andReturn( false ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( testData.getPhotoAuthor() ) ).andReturn( false ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN_1.getId() ) ).andReturn( true ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN_1 ) ).andReturn( true ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN_2.getId() ) ).andReturn( true ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( SUPER_ADMIN_2 ) ).andReturn( true ).anyTimes();


		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	private PhotoService getPhotoService() {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.load( testData.getPhoto().getId() ) ).andReturn( testData.getPhoto() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}
}
