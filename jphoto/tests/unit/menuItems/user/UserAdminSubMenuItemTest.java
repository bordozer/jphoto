package menuItems.user;

import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.user.items.UserAdminSubMenuItem;
import core.general.user.User;
import core.services.security.Services;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class UserAdminSubMenuItemTest extends AbstractUserMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeAdminSubMenuTest() {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem( testData.getUser(), User.NOT_LOGGED_USER, getServices() ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeAdminSubMenuOfAnotherUserTest() {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem( testData.getUser(), testData.getAccessor(), getServices() ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeAdminSubMenuOfAdminTest() {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem( SUPER_ADMIN_1, testData.getUser(), getServices() ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeOwnAdminSubMenuTest() {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem( testData.getUser(), testData.getUser(), getServices() ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeOwnAdminSubMenuTest() {
		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem( SUPER_ADMIN_1, SUPER_ADMIN_1, getServices() ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeAdminSubMenuOfUsualUserTest() {
		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserAdminSubMenuItem( testData.getUser(), SUPER_ADMIN_1, getServices() ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeAdminSubMenuForAnotherAdminTest() {
		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserAdminSubMenuItem( SUPER_ADMIN_2, SUPER_ADMIN_1, getServices() ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User accessor = SUPER_ADMIN_1;
		final Services services = getServices();

		final AbstractEntryMenuItemCommand command = new UserAdminSubMenuItem( testData.getUser(), accessor, services ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuText(), AbstractEntryMenuItem.ADMIN_SUB_MENU_ENTRY_TEXT );
		assertEquals( WRONG_COMMAND, command.getMenuCommand(), AbstractEntryMenuItem.ADMIN_SUB_MENU_ENTRY_COMMAND );
	}
}
