package menuItems.comment.operations;

import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.operations.CommentMenuItemEdit;
import com.bordozer.jphoto.unit.menuItems.comment.AbstractCommentMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentMenuItemEditTest extends AbstractCommentMenuItemTest_ {

    @Test
    public void onlyCommentAuthorCanEditCommentTest() {
        final User user = testData.getCommentAuthor();
        final Services services = getServices(user);

        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemEdit(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void photoAuthorCanNotEditCommentTest() {
        final User user = testData.getPhotoAuthor();
        final Services services = getServices(user);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void notLoggedUserCanNotEditCommentTest() {
        final User user = NOT_LOGGED_USER;
        final Services services = getServices(user);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void usualUserCanNotEditCommentTest() {
        final User user = testData.getAccessor();
        final Services services = getServices(user);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void adminCanNotEditCommentTest() {
        final User user = SUPER_ADMIN_1;
        final Services services = getServices(user);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void deletedCommentCanNotBeEditedTest() {
        final User user = testData.getCommentAuthor();
        final Services services = getServices(user);

        final PhotoComment comment = testData.getComment();
        comment.setCommentDeleted(true);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEdit(comment, user, services).isAccessibleFor());
    }

    @Test
    public void commandTest() {
        final User user = SUPER_ADMIN_1;
        final Services services = getServices(user);

        final AbstractEntryMenuItemCommand command = new CommentMenuItemEdit(testData.getComment(), user, services).getMenuItemCommand();

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated("CommentMenuItemEdit: Edit comment"));
        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format("editComment( %d );", testData.getComment().getId()));
    }
}

