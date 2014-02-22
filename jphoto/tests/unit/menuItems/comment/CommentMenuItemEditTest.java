package menuItems.comment;

import common.AbstractTestCase;
import core.general.menus.comment.items.CommentMenuItemEdit;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.security.Services;
import core.services.security.ServicesImpl;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentMenuItemEditTest extends AbstractTestCase {

	private final CommentMenuItemTestData testData = new CommentMenuItemTestData();

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void commentAuthorMenuTest() {

		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		assertTrue( CommentMenuItemDeleteTest.WRONG_MENU_TEXT, new CommentMenuItemEdit( testData.getComment(), user, services ).getMenuItemCommand().getMenuText().equals( "Edit your comment" ) );
	}

	@Test
	public void commentAuthorCanEditCommentTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		assertTrue( CommentMenuItemDeleteTest.MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemEdit( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotEditCommentTest() {
		final User user = testData.getPhotoAuthor();
		final Services services = getServices( testData, user );

		assertFalse( CommentMenuItemDeleteTest.MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void notLoggedUserCanNotEditCommentTest() {
		final User user = User.NOT_LOGGED_USER;
		final Services services = getServices( testData, user );

		assertFalse( CommentMenuItemDeleteTest.MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotEditCommentTest() {
		final User user = testData.getJustUser();
		final Services services = getServices( testData, user );

		assertFalse( CommentMenuItemDeleteTest.MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotEditCommentTest() {
		final User user = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, user );

		assertFalse( CommentMenuItemDeleteTest.MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void deletedCommentCanNotBeEditedTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		final PhotoComment comment = testData.getComment();
		comment.setCommentDeleted( true );

		assertFalse( CommentMenuItemDeleteTest.MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit( comment, user, services ).isAccessibleFor() );
	}

	private Services getServices( final CommentMenuItemTestData testData, final User user ) {
		final ServicesImpl services = new ServicesImpl();

		services.setPhotoCommentService( getPhotoCommentService() );
		services.setPhotoService( getPhotoService( testData ) );
		services.setSecurityService( getSecurityService( testData, user ) );

		return services;
	}

	private SecurityService getSecurityService( final CommentMenuItemTestData testData, final User user ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.userOwnThePhotoComment( user, testData.getComment() ) ).andReturn( testData.getComment().getCommentAuthor().getId() == user.getId() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	private PhotoService getPhotoService( final CommentMenuItemTestData testData ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.load( testData.getComment().getPhotoId() ) ).andReturn( testData.getPhoto() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}

	private PhotoCommentService getPhotoCommentService() {
		final PhotoCommentService photoCommentService = EasyMock.createMock( PhotoCommentService.class );

		EasyMock.expect( photoCommentService.load( testData.getComment().getId() ) ).andReturn( testData.getComment() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoCommentService );

		return photoCommentService;
	}
}

