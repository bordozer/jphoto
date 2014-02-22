package menuItems.comment;

import common.AbstractTestCase;
import core.general.menus.comment.items.CommentMenuItemDelete;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoService;
import core.services.security.Services;
import core.services.security.ServicesImpl;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentMenuItemDeleteTest extends AbstractTestCase {

	public static final String WRONG_MENU_TEXT = "Wrong menu text";

	public static final String MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT = "Menu item should be accessible but it is not";
	public static final String MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS = "Menu item should not be accessible but it is";

	private final CommentMenuItemTestData testData = new CommentMenuItemTestData();

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void menuCommandTextTest() {

		final Services services = getServices( testData );

		assertTrue( WRONG_MENU_TEXT, new CommentMenuItemDelete( testData.getComment(), testData.getCommentAuthor(), services ).getMenuItemCommand().getMenuText().equals( "Delete your comment" ) );
		assertTrue( WRONG_MENU_TEXT, new CommentMenuItemDelete( testData.getComment(), testData.getPhotoAuthor(), services ).getMenuItemCommand().getMenuText().equals( "Delete comment (as photo author)" ) );
	}

	@Test
	public void commentAuthorCanDeleteCommentTest() {
		final Services services = getServices( testData );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemDelete( testData.getComment(), testData.getCommentAuthor(), services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanDeleteCommentTest() {
		final Services services = getServices( testData );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemDelete( testData.getComment(), testData.getPhotoAuthor(), services ).isAccessibleFor() );
	}

	@Test
	public void notLoggedUserCanNotDeleteCommentTest() {
		final Services services = getServices( testData );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDelete( testData.getComment(), User.NOT_LOGGED_USER, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotDeleteCommentTest() {
		final Services services = getServices( testData );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDelete( testData.getComment(), testData.getJustUser(), services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotDeleteCommentTest() {
		final Services services = getServices( testData );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDelete( testData.getComment(), SUPER_MEGA_ADMIN, services ).isAccessibleFor() );
	}

	@Test
	public void deletedCommentCanNotBeDeletedAgainTest() {
		final Services services = getServices( testData );

		final PhotoComment comment = testData.getComment();
		comment.setCommentDeleted( true );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDelete( comment, testData.getCommentAuthor(), services ).isAccessibleFor() );
	}

	private Services getServices( final CommentMenuItemTestData testData ) {
		final ServicesImpl services = new ServicesImpl();

		services.setPhotoCommentService( getPhotoCommentService( testData ) );
		services.setPhotoService( getPhotoService( testData ) );

		return services;
	}

	private PhotoService getPhotoService( final CommentMenuItemTestData testData ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.load( testData.getComment().getPhotoId() ) ).andReturn( testData.getPhoto() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}

	private PhotoCommentService getPhotoCommentService( final CommentMenuItemTestData testData ) {
		final PhotoCommentService photoCommentService = EasyMock.createMock( PhotoCommentService.class );

		EasyMock.expect( photoCommentService.load( testData.getComment().getId() ) ).andReturn( testData.getComment() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoCommentService );

		return photoCommentService;
	}
}
