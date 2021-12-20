package menuItems.photo.admin;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin.PhotoAdminSubMenuItem;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhotoAdminSubMenuItemTest extends AbstractPhotoMenuItemTest_ {

    @Test
    public void notLoggedUserCanNotSeeAdminSubMenuTest() {
        final User accessor = NOT_LOGGED_USER;
        final Services services = getServices(accessor);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoAdminSubMenuItem(testData.getPhoto(), accessor, services).isAccessibleFor());
    }

    @Test
    public void usualUserCanNotSeeAdminSubMenuTest() {
        final User accessor = testData.getAccessor();
        final Services services = getServices(accessor);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoAdminSubMenuItem(testData.getPhoto(), accessor, services).isAccessibleFor());
    }

    @Test
    public void photoAuthorCanNotSeeAdminSubMenuTest() {
        final User accessor = testData.getPhotoAuthor();
        final Services services = getServices(accessor);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoAdminSubMenuItem(testData.getPhoto(), accessor, services).isAccessibleFor());
    }

    @Test
    public void adminCanSeeAdminSubMenuIfHeIsPhotoAuthorTest() {
        final User accessor = SUPER_ADMIN_1;

        final Photo photo = testData.getPhoto();
        photo.setUserId(accessor.getId());

        final Services services = getServices(accessor);

        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoAdminSubMenuItem(photo, accessor, services).isAccessibleFor());
    }

    @Test
    public void adminCanSeeAdminSubMenuTest() {
        final User accessor = SUPER_ADMIN_1;
        final Services services = getServices(accessor);

        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoAdminSubMenuItem(testData.getPhoto(), accessor, services).isAccessibleFor());
    }

    @Test
    public void adminCanSeeAdminSubMenuForPhotoOfAnotherAdminTest() {
        final User accessor = SUPER_ADMIN_1;
        final Services services = getServices(accessor);

        final Photo photo = testData.getPhoto();
        photo.setUserId(SUPER_ADMIN_2.getId());

        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoAdminSubMenuItem(photo, accessor, services).isAccessibleFor());
    }

    @Test
    public void commandTest() {
        final User accessor = SUPER_ADMIN_1;
        final Services services = getServices(accessor);

        final AbstractEntryMenuItemCommand command = new PhotoAdminSubMenuItem(testData.getPhoto(), accessor, services).getMenuItemCommand();

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated(AbstractEntryMenuItem.ADMIN_SUB_MENU_ENTRY_TEXT));
        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), AbstractEntryMenuItem.ADMIN_SUB_MENU_ENTRY_COMMAND);
    }
}
