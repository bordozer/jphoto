package entryMenu.comment;

import common.AbstractTestCase;
import core.general.menus.comment.items.CommentMenuItemEdit;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.photo.PhotoCommentService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CommentMenuItemEditTest extends AbstractTestCase {

	public static final String WRONG_MENU_TEXT = "Wrong menu text";

	private final TestData testData = new TestData();

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void commentAuthorMenuTest() {
		final CommentMenuItemEdit menuItem = new CommentMenuItemEdit();

		initServices( menuItem );

		assertTrue( WRONG_MENU_TEXT, menuItem.initMenuItemCommand( testData.comment.getId(), testData.commentAuthor ).getMenuText().equals( "Edit your comment" ) );
		assertTrue( WRONG_MENU_TEXT, menuItem.initMenuItemCommand( testData.comment.getId(), SUPER_MEGA_ADMIN ).getMenuText().equals( "Edit comment (ADMIN)" ) );
	}

	private void initServices( final CommentMenuItemEdit menuItem ) {
		menuItem.setPhotoCommentService( getPhotoCommentService() );
	}

	private PhotoCommentService getPhotoCommentService() {
		final PhotoCommentService photoCommentService = EasyMock.createMock( PhotoCommentService.class );

		EasyMock.expect( photoCommentService.load( testData.comment.getId() ) ).andReturn( testData.comment ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoCommentService );

		return photoCommentService;
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
