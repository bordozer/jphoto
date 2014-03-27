package menuItems.comment.admin;

import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.comment.admin.CommentAdminSubMenuItemLockCommentAuthor;
import core.general.user.User;
import core.services.security.Services;
import menuItems.comment.AbstractCommentMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentAdminSubMenuItemLockUserTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeLockUserSubMenuItemTest() {
		final User user = NOT_LOGGED_USER;
		final Services services = getServices( user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItemLockCommentAuthor( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeLockUserAdminSubMenuItemTest() {
		final User user = testData.getAccessor();
		final Services services = getServices( user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItemLockCommentAuthor( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotSeeLockUserAdminSubMenuItemTest() {
		final User user = testData.getPhotoAuthor();
		final Services services = getServices( user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItemLockCommentAuthor( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotSeeLockUserAdminSubMenuItemTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItemLockCommentAuthor( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeLockUserAdminSubMenuItemTest() {
		final User user = SUPER_ADMIN_1;
		final Services services = getServices( user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentAdminSubMenuItemLockCommentAuthor( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User user = SUPER_ADMIN_1;
		final Services services = getServices( user );

		final User commentAuthor = testData.getCommentAuthor();
		final AbstractEntryMenuItemCommand command = new CommentAdminSubMenuItemLockCommentAuthor( testData.getComment(), user, services ).getMenuItemCommand();

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated( String.format( "Lock comment author: %s", commentAuthor.getNameEscaped() ) ) );
		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format( "adminLockUser( %d, '%s' ); return false;", commentAuthor.getId(), commentAuthor.getNameEscaped() ) );
	}

	@Test
	public void cssClassTest() {
		final User user = NOT_LOGGED_USER; // does not matter
		final Services services = getServices( user );
		final CommentAdminSubMenuItemLockCommentAuthor menuItem = new CommentAdminSubMenuItemLockCommentAuthor( testData.getComment(), user, services );

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS_ADMIN );
	}
}
