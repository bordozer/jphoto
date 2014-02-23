package menuItems.comment;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.comment.items.CommentAdminSubMenuItemLockUser;
import core.general.user.User;
import core.services.security.Services;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentAdminSubMenuItemLockUserTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeLockUserSubMenuItemTest() {
		final User user = User.NOT_LOGGED_USER;
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItemLockUser( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeLockUserAdminSubMenuItemTest() {
		final User user = testData.getJustUser();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItemLockUser( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotSeeLockUserAdminSubMenuItemTest() {
		final User user = testData.getPhotoAuthor();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItemLockUser( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotSeeLockUserAdminSubMenuItemTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItemLockUser( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeLockUserAdminSubMenuItemTest() {
		final User user = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentAdminSubMenuItemLockUser( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User user = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, user );

		final User commentAuthor = testData.getCommentAuthor();
		final AbstractEntryMenuItemCommand command = new CommentAdminSubMenuItemLockUser( testData.getComment(), user, services ).getMenuItemCommand();

		assertEquals( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, command.getMenuText(), String.format( "Lock user: %s", commentAuthor.getNameEscaped() ) );
		assertEquals( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, command.getMenuCommand(), String.format( "adminLockUser( %d, '%s' ); return false;", commentAuthor.getId(), commentAuthor.getNameEscaped() ) );
	}
}
