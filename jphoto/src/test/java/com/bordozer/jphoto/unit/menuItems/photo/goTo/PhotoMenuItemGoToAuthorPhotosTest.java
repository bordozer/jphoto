package menuItems.photo.goTo;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.system.ServicesImpl;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.goTo.PhotoMenuItemGoToAuthorPhotos;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoMenuItemGoToAuthorPhotosTest extends AbstractPhotoMenuItemTest_ {

    @Test
    public void commandTest() {
        final menuItems.photo.goTo.GoToParameters parameters = new menuItems.photo.goTo.GoToParameters(testData.getAccessor(), 7);

        final ServicesImpl services = getServicesGoTo(parameters);

        final PhotoMenuItemGoToAuthorPhotos menuItem = new PhotoMenuItemGoToAuthorPhotos(testData.getPhoto(), parameters.getAccessor(), services);
        final AbstractEntryMenuItemCommand command = menuItem.getMenuItemCommand();

        final User photoAuthor = testData.getPhotoAuthor();

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated(String.format("PhotoMenuItem: %s: all photos ( %d )", photoAuthor.getNameEscaped(), parameters.getPhotosQty())));

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format("goToMemberPhotos( %d );", photoAuthor.getId()));

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS);
    }

    private ServicesImpl getServicesGoTo(final menuItems.photo.goTo.GoToParameters parameters) {
        final ServicesImpl services = getServices(parameters.getAccessor());
        services.setPhotoService(getPhotoService(parameters));

        return services;
    }

    private PhotoService getPhotoService(final menuItems.photo.goTo.GoToParameters parameters) {
        final PhotoService photoService = EasyMock.createMock(PhotoService.class);

        EasyMock.expect(photoService.getPhotosCountByUser(testData.getPhotoAuthor().getId())).andReturn(parameters.getPhotosQty()).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(photoService);

        return photoService;
    }
}
