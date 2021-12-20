package menuItems.photo.goTo;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.services.system.ServicesImpl;
import com.bordozer.jphoto.core.services.user.UserPhotoAlbumService;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.goTo.PhotoMenuItemGoToAuthorPhotoByAlbum;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoMenuItemGoToAuthorPhotoByAlbumTest extends AbstractPhotoMenuItemTest_ {

    private UserPhotoAlbum userPhotoAlbum;

    @Before
    public void setup() {
        super.setup();

        userPhotoAlbum = getUserPhotoAlbum();
    }

    @Test
    public void commandTest() {
        final menuItems.photo.goTo.GoToParameters parameters = new menuItems.photo.goTo.GoToParameters(testData.getAccessor(), 7);

        final ServicesImpl services = getServicesGoTo(parameters);

        final PhotoMenuItemGoToAuthorPhotoByAlbum menuItem = new PhotoMenuItemGoToAuthorPhotoByAlbum(testData.getPhoto(), parameters.getAccessor(), services, userPhotoAlbum);
        final AbstractEntryMenuItemCommand command = menuItem.getMenuItemCommand();

        final User photoAuthor = testData.getPhotoAuthor();

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated(String.format("PhotoMenuItem: %s: photos from album '%s' ( %s )", photoAuthor.getNameEscaped(), userPhotoAlbum.getName(), parameters.getPhotosQty())));

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format("goToMemberPhotosByAlbum( %d, %d );", photoAuthor.getId(), userPhotoAlbum.getId()));

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS);
    }

    private ServicesImpl getServicesGoTo(final menuItems.photo.goTo.GoToParameters parameters) {
        final ServicesImpl services = getServices(parameters.getAccessor());
        services.setUserPhotoAlbumService(getUserPhotoAlbumService(parameters));

        return services;
    }

    private UserPhotoAlbumService getUserPhotoAlbumService(final menuItems.photo.goTo.GoToParameters parameters) {
        final UserPhotoAlbumService userPhotoAlbumService = EasyMock.createMock(UserPhotoAlbumService.class);

        EasyMock.expect(userPhotoAlbumService.getUserPhotoAlbumPhotosQty(userPhotoAlbum.getId())).andReturn(parameters.getPhotosQty()).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(userPhotoAlbumService);

        return userPhotoAlbumService;
    }

    private UserPhotoAlbum getUserPhotoAlbum() {
        final UserPhotoAlbum album = new UserPhotoAlbum();

        album.setId(881);
        album.setName("Photo album");

        return album;
    }
}
