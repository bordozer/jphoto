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

	protected static final String WRONG_COMMAND = "Wrong menu text";

	protected static final String MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT = "Menu item should be accessible but it is not";
	protected static final String MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS = "Menu item should not be accessible but it is";

	protected PhotoMenuItemTestData testData;

	@Before
	public void setup() {
		super.setup();

		testData = new PhotoMenuItemTestData();
	}

	protected ServicesImpl getServices( final PhotoMenuItemTestData testData, final User accessor ) {
		final ServicesImpl services = new ServicesImpl();

		services.setPhotoService( getPhotoService( testData ) );
		services.setSecurityService( getSecurityService( accessor ) );
		services.setUserService( getUserService( testData ) );

		return services;
	}

	private UserService getUserService( final PhotoMenuItemTestData testData ) {
		final UserService userService = EasyMock.createMock( UserService.class );

		EasyMock.expect( userService.load( testData.getPhotoAuthor().getId() ) ).andReturn( testData.getPhotoAuthor() ).anyTimes();
		EasyMock.expect( userService.load( testData.getJustUser().getId() ) ).andReturn( testData.getJustUser() ).anyTimes();
		EasyMock.expect( userService.load( SUPER_MEGA_ADMIN.getId() ) ).andReturn( SUPER_MEGA_ADMIN ).anyTimes();
		EasyMock.expect( userService.load( SUPER_ADMIN.getId() ) ).andReturn( SUPER_ADMIN ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		return userService;
	}

	private SecurityService getSecurityService( final User accessor ) {
		final boolean isAdmin = SUPER_ADMIN.getId() == accessor.getId() || SUPER_MEGA_ADMIN.getId() == accessor.getId();

		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.isSuperAdminUser( accessor.getId() ) ).andReturn( isAdmin ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( accessor ) ).andReturn( isAdmin ).anyTimes();
		EasyMock.expect( securityService.userOwnThePhoto( accessor, testData.getPhoto().getId() ) ).andReturn( testData.getPhotoAuthor().getId() == accessor.getId() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	private PhotoService getPhotoService( final PhotoMenuItemTestData testData ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.load( testData.getPhoto().getId() ) ).andReturn( testData.getPhoto() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}
}
