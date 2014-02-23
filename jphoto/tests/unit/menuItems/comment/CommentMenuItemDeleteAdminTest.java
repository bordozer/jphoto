package menuItems.comment;

import core.general.menus.comment.items.CommentMenuItemDeleteAdmin;
import core.general.user.User;
import core.services.security.Services;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentMenuItemDeleteAdminTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeLockUserSubMenuItemTest() {
		final User user = User.NOT_LOGGED_USER;
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDeleteAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeLockUserAdminSubMenuItemTest() {
		final User user = testData.getJustUser();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDeleteAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotSeeLockUserAdminSubMenuItemTest() {
		final User user = testData.getPhotoAuthor();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDeleteAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotSeeLockUserAdminSubMenuItemTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDeleteAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeLockUserAdminSubMenuItemTest() {
		final User user = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemDeleteAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}
}
