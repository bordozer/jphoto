package entryMenu.comment;

import common.AbstractTestCase;
import core.general.menus.comment.items.CommentMenuItemDelete;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.photo.PhotoCommentService;
import core.services.security.SecurityService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CommentMenuItemDeleteTest extends AbstractTestCase {

	public static final String WRONG_MENU_TEXT = "Wrong menu text";

	private final TestData testData = new TestData();

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void commentAuthorMenuTest() {
		final CommentMenuItemDelete menuItem = new CommentMenuItemDelete();

		final User user = testData.commentAuthor;
		initServices( menuItem, user );

		assertTrue( WRONG_MENU_TEXT, menuItem.initMenuItemCommand( testData.comment.getId(), user ).getMenuText().equals( "Delete your comment" ) );
	}

	@Test
	public void photoAuthorMenuTest() {
		final CommentMenuItemDelete menuItem = new CommentMenuItemDelete();

		final User user = testData.photoAuthor;
		initServices( menuItem, user );

		assertTrue( WRONG_MENU_TEXT, menuItem.initMenuItemCommand( testData.comment.getId(), user ).getMenuText().equals( "Delete comment (as photo author)" ) );
	}

	@Test
	public void adminMenuTest() {
		final CommentMenuItemDelete menuItem = new CommentMenuItemDelete();

		final User user = SUPER_MEGA_ADMIN;
		initServices( menuItem, user );

		assertTrue( WRONG_MENU_TEXT, menuItem.initMenuItemCommand( testData.comment.getId(), user ).getMenuText().equals( "Delete comment (ADMIN)" ) );
	}

	private void initServices( final CommentMenuItemDelete menuItem, final User user ) {
		menuItem.setPhotoCommentService( getPhotoCommentService() );
		menuItem.setSecurityService( getSecurityService( user ) );
	}

	private PhotoCommentService getPhotoCommentService() {
		final PhotoCommentService photoCommentService = EasyMock.createMock( PhotoCommentService.class );

		EasyMock.expect( photoCommentService.load( testData.comment.getId() ) ).andReturn( testData.comment ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoCommentService );

		return photoCommentService;
	}

	private SecurityService getSecurityService( final User user ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.userOwnThePhoto( EasyMock.<User>anyObject(), EasyMock.anyInt() ) ).andReturn( user.getId() == testData.photoAuthor.getId() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	private class TestData {

		private final User commentAuthor;
		private final User photoAuthor;
		private final User justUser;

		private final Photo photo;

		private final PhotoComment comment;

		private TestData() {
			commentAuthor = new User( 111 );
			commentAuthor.setName( "commentAuthor" );

			photoAuthor = new User( 222 );
			commentAuthor.setName( "photoAuthor" );

			justUser = new User( 333 );
			commentAuthor.setName( "justUser" );

			photo = new Photo();
			photo.setId( 567 );
			photo.setName( "The photo" );
			photo.setUserId( photoAuthor.getId() );

			comment = new PhotoComment();
			comment.setId( 345 );
			comment.setPhotoId( photo.getId() );
			comment.setCommentAuthor( commentAuthor );
		}
	}
}
