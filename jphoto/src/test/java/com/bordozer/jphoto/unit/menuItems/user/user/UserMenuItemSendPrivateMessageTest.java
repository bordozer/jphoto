package menuItems.user.user;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.entry.FavoritesService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.ServicesImpl;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.user.user.UserMenuItemSendPrivateMessage;
import menuItems.user.AbstractUserMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserMenuItemSendPrivateMessageTest extends AbstractUserMenuItemTest_ {

    private User user;

    @Before
    public void setup() {
        super.setup();

        user = testData.getUser();
    }

    @Test
    public void notLoggedUserCanNotSeeMenuTest() {

        final Parameters parameters = new Parameters(NOT_LOGGED_USER, false);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserMenuItemSendPrivateMessage(user, NOT_LOGGED_USER, getServicesSendMessage(parameters)).isAccessibleFor());
    }

    @Test
    public void userCanNotSeeSendMessageInOwnMenuTest() {

        final Parameters parameters = new Parameters(user, false);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserMenuItemSendPrivateMessage(user, user, getServicesSendMessage(parameters)).isAccessibleFor());
    }

    @Test
    public void userCanNotSeeMenuOfUserIfHeIsInBlackListTest() {

        final Parameters parameters = new Parameters(testData.getAccessor(), true);

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserMenuItemSendPrivateMessage(user, testData.getAccessor(), getServicesSendMessage(parameters)).isAccessibleFor());
    }

    @Test
    public void userCanSeeMenuOfUserIfHeIsNotInBlackListTest() {

        final Parameters parameters = new Parameters(testData.getAccessor(), false);

        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserMenuItemSendPrivateMessage(user, testData.getAccessor(), getServicesSendMessage(parameters)).isAccessibleFor());
    }

    @Test
    public void userCanSeeMenuOfAdminIfHeIsNotInBlackListTest() {

        final Parameters parameters = new Parameters(SUPER_ADMIN_1, false);

        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserMenuItemSendPrivateMessage(user, SUPER_ADMIN_1, getServicesSendMessage(parameters)).isAccessibleFor());
    }

    @Test
    public void commandTest() {
        final User accessor = SUPER_ADMIN_1;

        final ServicesImpl services = getServices();

        final AbstractEntryMenuItemCommand command = new UserMenuItemSendPrivateMessage(user, accessor, services).getMenuItemCommand();

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated(String.format("Send private message to %s", user.getNameEscaped())));
        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format("sendPrivateMessage( %d, %d, '%s' );", accessor.getId(), user.getId(), user.getNameEscaped()));
    }

    private ServicesImpl getServicesSendMessage(final Parameters parameters) {
        final ServicesImpl services = getServices();

        services.setSecurityService(getSecurityService(parameters.getAccessor()));
        services.setFavoritesService(getFavoritesService(user, parameters.isAccessorIsInTheBlackListOfUser()));

        return services;
    }

    private SecurityService getSecurityService(final User user) {
        final SecurityService userService = EasyMock.createMock(SecurityService.class);

        EasyMock.expect(userService.isSuperAdminUser(user.getId())).andReturn(user.getId() == SUPER_ADMIN_1.getId()).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(userService);

        return userService;
    }

    private FavoritesService getFavoritesService(final User user, final boolean accessorIsInTheBlackListOfUser) {
        final FavoritesService favoritesService = EasyMock.createMock(FavoritesService.class);

        EasyMock.expect(favoritesService.isUserInBlackListOfUser(user.getId(), testData.getAccessor().getId())).andReturn(accessorIsInTheBlackListOfUser).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.replay(favoritesService);

        return favoritesService;
    }

    private static class Parameters {

        private final User accessor;
        private final boolean accessorIsInTheBlackListOfUser;

        private Parameters(final User accessor, final boolean accessorIsInTheBlackListOfUser) {
            this.accessor = accessor;
            this.accessorIsInTheBlackListOfUser = accessorIsInTheBlackListOfUser;
        }

        public User getAccessor() {
            return accessor;
        }

        public boolean isAccessorIsInTheBlackListOfUser() {
            return accessorIsInTheBlackListOfUser;
        }
    }
}
