package menuItems.comment;

import core.general.menus.comment.items.CommentMenuItemReply;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentMenuItemReplyTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotReplyCommentTest() {
		final User user = User.NOT_LOGGED_USER;
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemReply( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotReplyCommentTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemReply( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanReplyCommentTest() {
		final User user = testData.getPhotoAuthor();
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanReplyCommentTest() {
		final User user = testData.getJustUser();
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanReplyCommentTest() {
		final User user = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void everyoneCanReplyOnDeletedCommentTest() {
		final User user = testData.getJustUser();
		final Services services = getServices( testData, user );

		final PhotoComment comment = testData.getComment();
		comment.setCommentDeleted( true );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( comment, user, services ).isAccessibleFor() );
	}
}
