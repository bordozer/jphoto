package menuItems.comment;

import core.general.menus.comment.items.CommentMenuItemEdit;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentMenuItemEditTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void commentAuthorMenuTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		assertEquals( WRONG_MENU_TEXT, new CommentMenuItemEdit( testData.getComment(), user, services ).getMenuItemCommand().getMenuText(), "Edit your comment" );
	}

	@Test
	public void onlyCommentAuthorCanEditCommentTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemEdit( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotEditCommentTest() {
		final User user = testData.getPhotoAuthor();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void notLoggedUserCanNotEditCommentTest() {
		final User user = User.NOT_LOGGED_USER;
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotEditCommentTest() {
		final User user = testData.getJustUser();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotEditCommentTest() {
		final User user = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void deletedCommentCanNotBeEditedTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		final PhotoComment comment = testData.getComment();
		comment.setCommentDeleted( true );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit( comment, user, services ).isAccessibleFor() );
	}
}

