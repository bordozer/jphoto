package menuItems.comment.admin;

import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.admin.CommentAdminSubMenuItem;
import com.bordozer.jphoto.unit.menuItems.comment.AbstractCommentMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentAdminSubMenuItemTest extends AbstractCommentMenuItemTest_ {

    @Test
    public void notLoggedUserCanNotSeeAdminSubMenuTest() {
        final User accessor = NOT_LOGGED_USER;
        final Services services = getServices(accessor);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem(testData.getComment(), accessor, services).isAccessibleFor());
    }

    @Test
    public void usualUserCanNotSeeAdminSubMenuTest() {
        final User accessor = testData.getAccessor();
        final Services services = getServices(accessor);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem(testData.getComment(), accessor, services).isAccessibleFor());
    }

    @Test
    public void photoAuthorCanNotSeeAdminSubMenuTest() {
        final User accessor = testData.getPhotoAuthor();
        final Services services = getServices(accessor);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem(testData.getComment(), accessor, services).isAccessibleFor());
    }

    @Test
    public void commentAuthorCanNotSeeAdminSubMenuTest() {
        final User accessor = testData.getCommentAuthor();
        final Services services = getServices(accessor);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem(testData.getComment(), accessor, services).isAccessibleFor());
    }

    @Test
    public void adminCanNotSeeAdminSubMenuIfThereIsHisTest() {
        final User accessor = SUPER_ADMIN_1;
        final Services services = getServices(accessor);

        final PhotoComment comment = testData.getComment();
        comment.setCommentAuthor(accessor); // Admin see his own comment

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentAdminSubMenuItem(comment, accessor, services).isAccessibleFor());
    }

    @Test
    public void adminCanSeeAdminSubMenuTest() {
        final User accessor = SUPER_ADMIN_1;
        final Services services = getServices(accessor);

        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentAdminSubMenuItem(testData.getComment(), accessor, services).isAccessibleFor());
    }

    @Test
    public void adminCanSeeAdminSubMenuForCommentOfAnotherAdminTest() {
        final User accessor = SUPER_ADMIN_2;
        final Services services = getServices(accessor);

        final PhotoComment comment = testData.getComment();
        comment.setCommentAuthor(SUPER_ADMIN_1); // The comment of another com.bordozer.jphoto.admin

        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentAdminSubMenuItem(testData.getComment(), accessor, services).isAccessibleFor());
    }

    @Test
    public void commandTest() {
        final User accessor = SUPER_ADMIN_1;
        final Services services = getServices(accessor);

        final AbstractEntryMenuItemCommand command = new CommentAdminSubMenuItem(testData.getComment(), accessor, services).getMenuItemCommand();

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated(AbstractEntryMenuItem.ADMIN_SUB_MENU_ENTRY_TEXT));
        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), AbstractEntryMenuItem.ADMIN_SUB_MENU_ENTRY_COMMAND);
    }

    @Test
    public void cssClassTest() {
        final User user = NOT_LOGGED_USER; // does not matter
        final Services services = getServices(user);
        final CommentAdminSubMenuItem menuItem = new CommentAdminSubMenuItem(testData.getComment(), user, services);

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_ADMIN_CSS_CLASS);
    }
}
