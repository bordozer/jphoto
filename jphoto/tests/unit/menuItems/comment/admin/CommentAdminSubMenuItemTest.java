package menuItems.comment.admin;

import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.comment.admin.CommentAdminSubMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import menuItems.comment.AbstractCommentMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentAdminSubMenuItemTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeAdminSubMenuTest() {
		final User accessor = User.NOT_LOGGED_USER;
		final Services services = getServices( accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeAdminSubMenuTest() {
		final User accessor = testData.getAccessor();
		final Services services = getServices( accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotSeeAdminSubMenuTest() {
		final User accessor = testData.getPhotoAuthor();
		final Services services = getServices( accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotSeeAdminSubMenuTest() {
		final User accessor = testData.getCommentAuthor();
		final Services services = getServices( accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeAdminSubMenuIfThereIsHisTest() {
		final User accessor = SUPER_ADMIN_1;
		final Services services = getServices( accessor );

		final PhotoComment comment = testData.getComment();
		comment.setCommentAuthor( accessor ); // Admin see his own comment

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( comment, accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeAdminSubMenuTest() {
		final User accessor = SUPER_ADMIN_1;
		final Services services = getServices( accessor );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentAdminSubMenuItem( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeAdminSubMenuForCommentOfAnotherAdminTest() {
		final User accessor = SUPER_ADMIN_2;
		final Services services = getServices( accessor );

		final PhotoComment comment = testData.getComment();
		comment.setCommentAuthor( SUPER_ADMIN_1 ); // The comment of another admin

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentAdminSubMenuItem( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User accessor = SUPER_ADMIN_1;
		final Services services = getServices( accessor );

		final AbstractEntryMenuItemCommand command = new CommentAdminSubMenuItem( testData.getComment(), accessor, services ).getMenuItemCommand();

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated( AbstractEntryMenuItem.ADMIN_SUB_MENU_ENTRY_TEXT ) );
		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), AbstractEntryMenuItem.ADMIN_SUB_MENU_ENTRY_COMMAND );
	}

	@Test
	public void cssClassTest() {
		final User user = User.NOT_LOGGED_USER; // does not matter
		final Services services = getServices( user );
		final CommentAdminSubMenuItem menuItem = new CommentAdminSubMenuItem( testData.getComment(), user, services );

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS_ADMIN );
	}
}
