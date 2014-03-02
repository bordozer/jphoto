package menuItems.user.admin;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.user.admin.UserAdminSubMenuItemLockUser;
import core.general.user.User;
import core.services.security.Services;
import menuItems.user.AbstractUserMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserAdminSubMenuItemLockUserTest extends AbstractUserMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeLockUserSubMenuItemTest() {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItemLockUser( testData.getUser(), User.NOT_LOGGED_USER, getServices() ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeLockUserAdminSubMenuItemOfAnotherUserTest() {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItemLockUser( testData.getUser(), testData.getAccessor(), getServices() ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeOwnLockUserMenuTest() {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItemLockUser( testData.getUser(), testData.getUser(), getServices() ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeLockUserAdminSubMenuItemOfAdminTest() {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItemLockUser( SUPER_ADMIN_1, testData.getUser(), getServices() ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeLockAnotherAdminAdminSubMenuItemTest() {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItemLockUser( SUPER_ADMIN_2, SUPER_ADMIN_1, getServices() ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeOwnLockUserMenuItemTest() {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItemLockUser( SUPER_ADMIN_1, SUPER_ADMIN_1, getServices() ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeLockUserAdminSubMenuItemTest() {
		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserAdminSubMenuItemLockUser( testData.getUser(), SUPER_ADMIN_1, getServices() ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User accessor = SUPER_ADMIN_1;
		final Services services = getServices();

		final AbstractEntryMenuItemCommand command = new UserAdminSubMenuItemLockUser( testData.getUser(), accessor, services ).getMenuItemCommand();

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated( String.format( "Lock member: %s", testData.getUser().getNameEscaped() ) ) );
		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format( "adminLockUser( %d, '%s' ); return false;", testData.getUser().getId(), testData.getUser().getNameEscaped() ) );
	}
}
