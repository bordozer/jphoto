package menuItems.comment;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.comment.items.CommentAdminSubMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentAdminSubMenuItemTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeAdminSubMenuTest() {
		final User accessor = User.NOT_LOGGED_USER;
		final Services services = getServices( testData, accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeAdminSubMenuTest() {
		final User accessor = testData.getAccessor();
		final Services services = getServices( testData, accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotSeeAdminSubMenuTest() {
		final User accessor = testData.getPhotoAuthor();
		final Services services = getServices( testData, accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotSeeAdminSubMenuTest() {
		final User accessor = testData.getCommentAuthor();
		final Services services = getServices( testData, accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeAdminSubMenuIfThereIsHisTest() {
		final User accessor = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, accessor );

		final PhotoComment comment = testData.getComment();
		comment.setCommentAuthor( accessor ); // Admin see his own comment

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( comment, accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeAdminSubMenuTest() {
		final User accessor = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, accessor );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentAdminSubMenuItem( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeAdminSubMenuForCommentOfAnotherAdminTest() {
		final User accessor = SUPER_ADMIN;
		final Services services = getServices( testData, accessor );

		final PhotoComment comment = testData.getComment();
		comment.setCommentAuthor( SUPER_MEGA_ADMIN ); // The comment of another admin

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentAdminSubMenuItem( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User accessor = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, accessor );

		final AbstractEntryMenuItemCommand command = new CommentAdminSubMenuItem( testData.getComment(), accessor, services ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuText(), "ADMIN" );
		assertEquals( WRONG_COMMAND, command.getMenuCommand(), "return false;" );
	}
}
