package menuItems.photo.goTo;

import com.bordozer.jphoto.core.enums.UserTeamMemberType;
import com.bordozer.jphoto.core.general.photoTeam.PhotoTeamMember;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.system.ServicesImpl;
import com.bordozer.jphoto.core.services.user.UserTeamService;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.goTo.PhotoMenuItemGoToAuthorPhotoByTeamMember;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoMenuItemGoToAuthorPhotoByTeamMemberTest extends AbstractPhotoMenuItemTest_ {

    private PhotoTeamMember photoTeamMember;
    private UserTeamMember userTeamMember;

    @Before
    public void setup() {
        super.setup();

        photoTeamMember = getPhotoTeamMember();
        userTeamMember = getUserTeamMember();
    }

    @Test
    public void commandTest() {
        final menuItems.photo.goTo.GoToParameters parameters = new menuItems.photo.goTo.GoToParameters(testData.getAccessor(), 7);

        final ServicesImpl services = getServicesGoTo(parameters);

        final PhotoMenuItemGoToAuthorPhotoByTeamMember menuItem = new PhotoMenuItemGoToAuthorPhotoByTeamMember(testData.getPhoto(), parameters.getAccessor(), services, photoTeamMember);
        final AbstractEntryMenuItemCommand command = menuItem.getMenuItemCommand();

        final User photoAuthor = testData.getPhotoAuthor();

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated(String.format("PhotoMenuItem: %s: photos with %s %s ( %d )", photoAuthor.getNameEscaped(), userTeamMember.getTeamMemberType().getName().toLowerCase(), userTeamMember.getTeamMemberName(), parameters.getPhotosQty())));

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format("goToMemberPhotosByTeamMember( %d, %d );", photoAuthor.getId(), userTeamMember.getId()));

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS);
    }

    private ServicesImpl getServicesGoTo(final menuItems.photo.goTo.GoToParameters parameters) {
        final ServicesImpl services = getServices(parameters.getAccessor());
        services.setUserTeamService(getUserTeamService(parameters.getPhotosQty()));

        return services;
    }

    protected UserTeamService getUserTeamService(final int teamMemberPhotosQty) {
        final UserTeamService userTeamService = EasyMock.createMock(UserTeamService.class);

        EasyMock.expect(userTeamService.getTeamMemberPhotosQty(userTeamMember.getId())).andReturn(teamMemberPhotosQty).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(userTeamService);

        return userTeamService;
    }

    private PhotoTeamMember getPhotoTeamMember() {
        final PhotoTeamMember photoTeamMember = new PhotoTeamMember();
        photoTeamMember.setUserTeamMember(getUserTeamMember());

        return photoTeamMember;
    }

    private UserTeamMember getUserTeamMember() {
        final UserTeamMember userTeamMember = new UserTeamMember();

        userTeamMember.setId(951);
        userTeamMember.setName("Team Member");
        userTeamMember.setTeamMemberUser(getTeamMemberUser());
        userTeamMember.setTeamMemberType(UserTeamMemberType.MODEL);

        return userTeamMember;
    }

    private User getTeamMemberUser() {
        final User user = new User();
        user.setName("Photo team member user");

        return user;
    }
}
