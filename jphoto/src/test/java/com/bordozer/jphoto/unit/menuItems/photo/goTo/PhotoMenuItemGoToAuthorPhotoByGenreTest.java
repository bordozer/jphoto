package menuItems.photo.goTo;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.system.ServicesImpl;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.goTo.PhotoMenuItemGoToAuthorPhotoByGenre;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoMenuItemGoToAuthorPhotoByGenreTest extends AbstractPhotoMenuItemTest_ {

    @Test
    public void commandTest() {
        final User accessor = new User();
        final menuItems.photo.goTo.GoToParameters parameters = new menuItems.photo.goTo.GoToParameters(testData.getAccessor(), 7);

        final ServicesImpl services = getServicesGoTo(parameters);

        final PhotoMenuItemGoToAuthorPhotoByGenre menuItem = new PhotoMenuItemGoToAuthorPhotoByGenre(testData.getPhoto(), parameters.getAccessor(), services);
        final AbstractEntryMenuItemCommand command = menuItem.getMenuItemCommand();

        final User photoAuthor = testData.getPhotoAuthor();
        final Genre genre = testData.getGenre();

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated(String.format("PhotoMenuItem: %s: photos in category '%s' ( %s )", photoAuthor.getNameEscaped(), translatorService.translateGenre(testData.getGenre(), AbstractTestCase.MENU_LANGUAGE), parameters.getPhotosQty())));

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format("goToMemberPhotosByGenre( %d, %d );", photoAuthor.getId(), genre.getId()));

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS);
    }

    private ServicesImpl getServicesGoTo(final menuItems.photo.goTo.GoToParameters parameters) {
        final ServicesImpl services = getServices(parameters.getAccessor());
        services.setGenreService(getGenreService());
        services.setPhotoService(getPhotoService(parameters));

        return services;
    }

    private GenreService getGenreService() {
        final GenreService genreService = EasyMock.createMock(GenreService.class);

        EasyMock.expect(genreService.load(testData.getPhoto().getGenreId())).andReturn(testData.getGenre()).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(genreService);

        return genreService;
    }

    private PhotoService getPhotoService(final menuItems.photo.goTo.GoToParameters parameters) {
        final PhotoService photoService = EasyMock.createMock(PhotoService.class);

        EasyMock.expect(photoService.getPhotosCountByUserAndGenre(testData.getPhotoAuthor().getId(), testData.getPhoto().getGenreId())).andReturn(parameters.getPhotosQty()).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(photoService);

        return photoService;
    }
}
