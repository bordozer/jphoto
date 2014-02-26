package menuItems.user;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.user.items.UserAdminSubMenuItem;
import core.general.user.User;
import core.services.security.Services;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserAdminSubMenuItemTest extends AbstractUserMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeAdminSubMenuTest() {
		final User accessor = User.NOT_LOGGED_USER;
		final Services services = getServices();

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeAdminSubMenuTest() {
		final User accessor = testData.getAccessor();
		final Services services = getServices();

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotSeeAdminSubMenuTest() {
		final User accessor = testData.getAccessor();
		final Services services = getServices();

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotSeeAdminSubMenuTest() {
		final User accessor = testData.getAccessor();
		final Services services = getServices();

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeAdminSubMenuIfThereIsHisTest() {
		final User accessor = SUPER_ADMIN_1;
		final Services services = getServices();

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem( SUPER_ADMIN_1, accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeAdminSubMenuTest() {
		final User accessor = SUPER_ADMIN_1;
		final Services services = getServices();

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserAdminSubMenuItem( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeAdminSubMenuForCommentOfAnotherAdminTest() {
		final User accessor = SUPER_ADMIN_2;
		final Services services = getServices();

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserAdminSubMenuItem( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User accessor = SUPER_ADMIN_1;
		final Services services = getServices();

		final AbstractEntryMenuItemCommand command = new UserAdminSubMenuItem( testData.getUser(), accessor, services ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuText(), "ADMIN" );
		assertEquals( WRONG_COMMAND, command.getMenuCommand(), "return false;" );
	}
}
