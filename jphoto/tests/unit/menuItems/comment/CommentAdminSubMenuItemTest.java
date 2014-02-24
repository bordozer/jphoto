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
		final User user = User.NOT_LOGGED_USER;
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeAdminSubMenuTest() {
		final User user = testData.getJustUser();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotSeeAdminSubMenuTest() {
		final User user = testData.getPhotoAuthor();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotSeeAdminSubMenuTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeAdminSubMenuIfThereIsHisTest() {
		final User user = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, user );

		final PhotoComment comment = testData.getComment();
		comment.setCommentAuthor( user ); // Admin see his own comment

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem( comment, user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeAdminSubMenuTest() {
		final User user = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentAdminSubMenuItem( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeAdminSubMenuForCommentOfAnotherAdminTest() {
		final User user = SUPER_ADMIN;
		final Services services = getServices( testData, user );

		final PhotoComment comment = testData.getComment();
		comment.setCommentAuthor( SUPER_MEGA_ADMIN ); // The comment of another admin

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentAdminSubMenuItem( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User user = SUPER_MEGA_ADMIN;
		final Services services = getServices( testData, user );

		final AbstractEntryMenuItemCommand command = new CommentAdminSubMenuItem( testData.getComment(), user, services ).getMenuItemCommand();

		assertEquals( WRONG_MENU_TEXT, command.getMenuText(), "ADMIN" );
		assertEquals( WRONG_MENU_TEXT, command.getMenuCommand(), "return false;" );
	}
}
