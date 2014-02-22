package menuItems.comment;

import common.AbstractTestCase;
import core.general.menus.comment.items.CommentMenuItemDelete;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.photo.PhotoCommentService;
import core.services.security.Services;
import core.services.security.ServicesImpl;
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

		final Services services = getServices( testData );

		assertTrue( WRONG_MENU_TEXT, new CommentMenuItemDelete( testData.comment, testData.commentAuthor, services ).getMenuItemCommand().getMenuText().equals( "Delete your comment" ) );
		assertTrue( WRONG_MENU_TEXT, new CommentMenuItemDelete( testData.comment, testData.photoAuthor, services ).getMenuItemCommand().getMenuText().equals( "Delete comment (as photo author)" ) );
	}

	private Services getServices( final TestData testData ) {
		final ServicesImpl services = new ServicesImpl();

		services.setPhotoCommentService( getPhotoCommentService() );

		return services;
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
