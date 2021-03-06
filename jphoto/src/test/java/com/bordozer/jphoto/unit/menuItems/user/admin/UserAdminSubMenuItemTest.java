package menuItems.user.admin;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.user.admin.UserAdminSubMenuItem;
import menuItems.user.AbstractUserMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserAdminSubMenuItemTest extends AbstractUserMenuItemTest_ {

    @Test
    public void notLoggedUserCanNotSeeAdminSubMenuTest() {
        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem(testData.getUser(), NOT_LOGGED_USER, getServices()).isAccessibleFor());
    }

    @Test
    public void usualUserCanNotSeeAdminSubMenuOfAnotherUserTest() {
        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem(testData.getUser(), testData.getAccessor(), getServices()).isAccessibleFor());
    }

    @Test
    public void usualUserCanNotSeeAdminSubMenuOfAdminTest() {
        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem(SUPER_ADMIN_1, testData.getUser(), getServices()).isAccessibleFor());
    }

    @Test
    public void usualUserCanNotSeeOwnAdminSubMenuTest() {
        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem(testData.getUser(), testData.getUser(), getServices()).isAccessibleFor());
    }

    @Test
    public void adminCanNotSeeOwnAdminSubMenuTest() {
        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserAdminSubMenuItem(SUPER_ADMIN_1, SUPER_ADMIN_1, getServices()).isAccessibleFor());
    }

    @Test
    public void adminCanSeeAdminSubMenuOfUsualUserTest() {
        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserAdminSubMenuItem(testData.getUser(), SUPER_ADMIN_1, getServices()).isAccessibleFor());
    }

    @Test
    public void adminCanSeeAdminSubMenuForAnotherAdminTest() {
        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserAdminSubMenuItem(SUPER_ADMIN_2, SUPER_ADMIN_1, getServices()).isAccessibleFor());
    }

    @Test
    public void commandTest() {
        final User accessor = SUPER_ADMIN_1;
        final Services services = getServices();

        final AbstractEntryMenuItemCommand command = new UserAdminSubMenuItem(testData.getUser(), accessor, services).getMenuItemCommand();

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated(AbstractEntryMenuItem.ADMIN_SUB_MENU_ENTRY_TEXT));
        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), AbstractEntryMenuItem.ADMIN_SUB_MENU_ENTRY_COMMAND);
    }
}
