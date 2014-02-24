package menuItems.comment;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.comment.items.CommentMenuItemDeleteAdmin;
import core.general.user.User;
import core.services.security.Services;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentMenuItemDeleteAdminTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotDeleteCommentAdminSubMenuItemTest() {
		final User user = User.NOT_LOGGED_USER;
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDeleteAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeDeleteCommentAdminSubMenuItemTest() {
		final User user = testData.getAccessor();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDeleteAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotSeeDeleteCommentAdminSubMenuItemTest() {
		final User user = testData.getPhotoAuthor();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDeleteAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotSeeDeleteCommentAdminSubMenuItemTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDeleteAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeDeleteCommentAdminSubMenuItemTest() {
		final User user = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemDeleteAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User user = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, user );

		final AbstractEntryMenuItemCommand command = new CommentMenuItemDeleteAdmin( testData.getComment(), user, services ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuText(), "Delete comment (ADMIN)" );
		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "deleteComment( %d ); return false;", testData.getComment().getId() ) );
	}
}
