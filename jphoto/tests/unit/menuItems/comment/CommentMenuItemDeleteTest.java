package menuItems.comment;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.comment.items.CommentMenuItemDelete;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentMenuItemDeleteTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void commentAuthorCanDeleteCommentTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemDelete( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanDeleteCommentTest() {
		final User user = testData.getPhotoAuthor();
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemDelete( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void notLoggedUserCanNotDeleteCommentTest() {
		final User user = User.NOT_LOGGED_USER;
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDelete( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotDeleteCommentTest() {
		final User user = testData.getAccessor();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDelete( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotDeleteCommentTest() {
		final User user = SUPER_ADMIN_1;
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDelete( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void deletedCommentCanNotBeDeletedAgainTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		final PhotoComment comment = testData.getComment();
		comment.setCommentDeleted( true );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDelete( comment, user, services ).isAccessibleFor() );
	}

	@Test
	public void ownCommentTextTest() {

		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		assertEquals( WRONG_COMMAND, new CommentMenuItemDelete( testData.getComment(), user, services ).getMenuItemCommand().getMenuText(), "Delete comment" );
	}

	@Test
	public void photoOwnerCommentTextTest() {

		final User user = testData.getPhotoAuthor();
		final Services services = getServices( testData, user );

		assertEquals( WRONG_COMMAND, new CommentMenuItemDelete( testData.getComment(), user, services ).getMenuItemCommand().getMenuText(), "Delete comment (as photo author)" );
	}

	@Test
	public void commandTest() {

		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		final AbstractEntryMenuItemCommand command = new CommentMenuItemDelete( testData.getComment(), user, services ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "deleteComment( %d ); return false;", testData.getComment().getId() ) );
	}
}
