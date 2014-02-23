package menuItems.comment;

import common.AbstractTestCase;
import core.general.user.User;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import core.services.user.UserService;
import org.easymock.EasyMock;
import org.junit.Before;

public class AbstractCommentMenuItemTest_ extends AbstractTestCase {

	public static final String WRONG_MENU_TEXT = "Wrong menu text";
	public static final String MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT = "Menu item should be accessible but it is not";
	public static final String MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS = "Menu item should not be accessible but it is";

	protected CommentMenuItemTestData testData;

	@Before
	public void setup() {
		super.setup();

		testData = new CommentMenuItemTestData();
	}

	protected ServicesImpl getServices( final CommentMenuItemTestData testData, final User user ) {
		final ServicesImpl services = new ServicesImpl();

		services.setPhotoCommentService( getPhotoCommentService( testData ) );
		services.setPhotoService( getPhotoService( testData ) );
		services.setSecurityService( getSecurityService( testData, user ) );
		services.setUserService( getUserService( testData ) );

		return services;
	}

	private UserService getUserService( final CommentMenuItemTestData testData ) {
		final UserService userService = EasyMock.createMock( UserService.class );

		EasyMock.expect( userService.load( testData.getJustUser().getId() ) ).andReturn( testData.getJustUser() ).anyTimes();
		EasyMock.expect( userService.load( testData.getCommentAuthor().getId() ) ).andReturn( testData.getCommentAuthor() ).anyTimes();
		EasyMock.expect( userService.load( testData.getPhotoAuthor().getId() ) ).andReturn( testData.getPhotoAuthor() ).anyTimes();
		EasyMock.expect( userService.load( SUPER_MEGA_ADMIN.getId() ) ).andReturn( SUPER_MEGA_ADMIN ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		return userService;
	}

	private SecurityService getSecurityService( final CommentMenuItemTestData testData, final User user ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.userOwnThePhotoComment( user, testData.getComment() ) ).andReturn( testData.getComment().getCommentAuthor().getId() == user.getId() ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( user.getId() ) ).andReturn( SUPER_ADMIN.getId() == user.getId() || SUPER_MEGA_ADMIN.getId() == user.getId() ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( user ) ).andReturn( SUPER_ADMIN.getId() == user.getId() || SUPER_MEGA_ADMIN.getId() == user.getId() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	protected PhotoService getPhotoService( final CommentMenuItemTestData testData ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.load( testData.getComment().getPhotoId() ) ).andReturn( testData.getPhoto() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}

	protected PhotoCommentService getPhotoCommentService( final CommentMenuItemTestData testData ) {
		final PhotoCommentService photoCommentService = EasyMock.createMock( PhotoCommentService.class );

		EasyMock.expect( photoCommentService.load( testData.getComment().getId() ) ).andReturn( testData.getComment() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoCommentService );

		return photoCommentService;
	}
}
