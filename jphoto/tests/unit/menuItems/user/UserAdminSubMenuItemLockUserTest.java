package menuItems.user;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.user.items.UserAdminSubMenuItemLockUser;
import core.general.user.User;
import core.services.security.Services;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserAdminSubMenuItemLockUserTest extends AbstractUserMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeLockUserSubMenuItemTest() {
		final User accessor = User.NOT_LOGGED_USER;
		final Services services = getServices();

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItemLockUser( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeLockUserAdminSubMenuItemTest() {
		final User accessor = testData.getAccessor();
		final Services services = getServices();

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItemLockUser( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotSeeLockUserAdminSubMenuItemTest() {
		final User accessor = testData.getAccessor();
		final Services services = getServices();

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItemLockUser( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotSeeLockUserAdminSubMenuItemTest() {
		final User accessor = testData.getAccessor();
		final Services services = getServices();

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItemLockUser( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeLockAnotherAdminAdminSubMenuItemTest() {
		final User accessor = SUPER_MEGA_ADMIN;
		final Services services = getServices();

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItemLockUser( SUPER_ADMIN, accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeLockUserAdminSubMenuItemTest() {
		final User accessor = SUPER_MEGA_ADMIN;
		final Services services = getServices();

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserAdminSubMenuItemLockUser( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User accessor = SUPER_MEGA_ADMIN;
		final Services services = getServices();

		final AbstractEntryMenuItemCommand command = new UserAdminSubMenuItemLockUser( testData.getUser(), accessor, services ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuText(), String.format( "Lock user: %s", testData.getUser().getNameEscaped() ) );
		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "adminLockUser( %d, '%s' ); return false;", testData.getUser().getId(), testData.getUser().getNameEscaped() ) );
	}
}
