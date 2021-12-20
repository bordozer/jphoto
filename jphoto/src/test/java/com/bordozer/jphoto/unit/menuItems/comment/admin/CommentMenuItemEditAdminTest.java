package menuItems.comment.admin;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.system.ServicesImpl;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.admin.CommentMenuItemEditAdmin;
import com.bordozer.jphoto.unit.menuItems.comment.AbstractCommentMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentMenuItemEditAdminTest extends AbstractCommentMenuItemTest_ {

    @Test
    public void notLoggedUserCanNotSeeEditCommentAdminSubMenuItemTest() {
        final User user = NOT_LOGGED_USER;
        final Services services = getServicesForTest(user);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEditAdmin(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void usualUserCanNotSeeEditCommentAdminSubMenuItemTest() {
        final User user = testData.getAccessor();
        final Services services = getServicesForTest(user);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEditAdmin(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void photoAuthorCanNotSeeEditCommentAdminSubMenuItemTest() {
        final User user = testData.getPhotoAuthor();
        final Services services = getServicesForTest(user);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEditAdmin(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void commentAuthorCanNotSeeEditCommentAdminSubMenuItemTest() {
        final User user = testData.getCommentAuthor();
        final Services services = getServicesForTest(user);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEditAdmin(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void deletedCommentCanNotBeEditedTest() {
        final User user = SUPER_ADMIN_1;
        final Services services = getServicesForTest(user);

        final PhotoComment comment = testData.getComment();
        comment.setCommentDeleted(true);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEditAdmin(comment, user, services).isAccessibleFor());
    }

    @Test
    public void adminCanNotSeeEditCommentAdminSubMenuItemIfItIsSwitchedOFFTest() {
        final User user = SUPER_ADMIN_1;
        final Services services = getServicesForTest(user);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEditAdmin(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void adminCanSeeEditCommentAdminSubMenuItemIfItIsSwitchedONTest() {
        final User user = SUPER_ADMIN_1;
        final Services services = getServicesForTest(user, true);

        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemEditAdmin(testData.getComment(), user, services).isAccessibleFor());
    }

    @Test
    public void commandTest() {
        final User user = SUPER_ADMIN_1;
        final Services services = getServices(user);

        final AbstractEntryMenuItemCommand command = new CommentMenuItemEditAdmin(testData.getComment(), user, services).getMenuItemCommand();

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated("CommentMenuItemEdit: Edit comment"));
        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format("editComment( %d );", testData.getComment().getId()));
    }

    @Test
    public void cssClassTest() {
        final User user = NOT_LOGGED_USER; // does not matter
        final Services services = getServices(user);
        final CommentMenuItemEditAdmin menuItem = new CommentMenuItemEditAdmin(testData.getComment(), user, services);

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_ADMIN_CSS_CLASS);
    }

    private Services getServicesForTest(final User user) {
        return getServicesForTest(user, false);
    }

    private Services getServicesForTest(final User user, final Boolean adminCanEditComments) {
        final ServicesImpl services = getServices(user);

        services.setConfigurationService(getConfigurationService(adminCanEditComments));

        return services;
    }

    private ConfigurationService getConfigurationService(final Boolean adminCanEditComments) {
        final ConfigurationService configurationService = EasyMock.createMock(ConfigurationService.class);

        EasyMock.expect(configurationService.getBoolean(ConfigurationKey.ADMIN_CAN_EDIT_PHOTO_COMMENTS)).andReturn(adminCanEditComments).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(configurationService);
        return configurationService;
    }
}
