package menuItems.comment.admin;

import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.admin.CommentMenuItemDeleteAdmin;
import com.bordozer.jphoto.unit.menuItems.comment.AbstractCommentMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentMenuItemDeleteAdminTest extends AbstractCommentMenuItemTest_ {

    @Test
    public void notLoggedUserCanNotDeleteCommentAdminSubMenuItemTest() {
        final User user = NOT_LOGGED_USER;
        final Services services = getServices(user);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDeleteAdmin(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void usualUserCanNotSeeDeleteCommentAdminSubMenuItemTest() {
        final User user = testData.getAccessor();
        final Services services = getServices(user);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDeleteAdmin(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void photoAuthorCanNotSeeDeleteCommentAdminSubMenuItemTest() {
        final User user = testData.getPhotoAuthor();
        final Services services = getServices(user);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDeleteAdmin(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void commentAuthorCanNotSeeDeleteCommentAdminSubMenuItemTest() {
        final User user = testData.getCommentAuthor();
        final Services services = getServices(user);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDeleteAdmin(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void adminCanSeeMenuTest() {
        final User user = SUPER_ADMIN_1;
        final Services services = getServices(user);

        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemDeleteAdmin(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void adminCanNotSeeMenuIfCommentHasAlreadyBeenDeletedTest() {
        final User user = SUPER_ADMIN_1;
        final Services services = getServices(user);

        final PhotoComment comment = testData.getComment();
        comment.setCommentDeleted(true);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemDeleteAdmin(comment, user, services).isAccessibleFor());
    }

    @Test
    public void commandTest() {
        final User user = SUPER_ADMIN_1;
        final Services services = getServices(user);

        final AbstractEntryMenuItemCommand command = new CommentMenuItemDeleteAdmin(testData.getComment(), user, services).getMenuItemCommand();

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated("CommentMenuItemDelete: Delete comment"));
        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format("deleteComment( %d );", testData.getComment().getId()));
    }

    @Test
    public void cssClassTest() {
        final User user = NOT_LOGGED_USER; // does not matter
        final Services services = getServices(user);
        final CommentMenuItemDeleteAdmin menuItem = new CommentMenuItemDeleteAdmin(testData.getComment(), user, services);

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_ADMIN_CSS_CLASS);
    }
}
