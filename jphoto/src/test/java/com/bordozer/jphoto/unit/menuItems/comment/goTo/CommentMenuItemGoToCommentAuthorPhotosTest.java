package menuItems.comment.goTo;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.system.ServicesImpl;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.goTo.CommentMenuItemGoToCommentAuthorPhotos;
import com.bordozer.jphoto.unit.menuItems.comment.AbstractCommentMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommentMenuItemGoToCommentAuthorPhotosTest extends AbstractCommentMenuItemTest_ {

    @Test
    public void photoQty_Is_getPhotoQtyByUser_method1Test() {
        final int photosQty = 0;
        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, getMenuEntry(new Parameters(testData.getAccessor(), photosQty)).getPhotoQty(), photosQty);
    }

    @Test
    public void photoQty_Is_getPhotoQtyByUser_method2Test() {
        final int photosQty = 7;
        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, getMenuEntry(new Parameters(testData.getAccessor(), photosQty)).getPhotoQty(), photosQty);
    }

    @Test
    public void commandTest() {
        final int photosQty = 4;

        final CommentMenuItemGoToCommentAuthorPhotos menuEntry = getMenuEntry(new Parameters(testData.getAccessor(), photosQty));
        final AbstractEntryMenuItemCommand menuItemCommand = menuEntry.getMenuItemCommand();

        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItemCommand.getMenuText(), translated(String.format("PhotoMenuItem: %s: all photos ( %s )", testData.getCommentAuthor().getNameEscaped(), photosQty)));
        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItemCommand.getMenuCommand(), String.format("goToMemberPhotos( %d );", testData.getCommentAuthor().getId()));
    }

    private CommentMenuItemGoToCommentAuthorPhotos getMenuEntry(final Parameters parameters) {
        final ServicesImpl services = getServicesForTest(parameters);
        return new CommentMenuItemGoToCommentAuthorPhotos(testData.getComment(), parameters.getAccessor(), services);
    }

    private ServicesImpl getServicesForTest(final Parameters parameters) {
        final ServicesImpl services = getServices(parameters.getAccessor());

        services.setPhotoService(getPhotoServiceForTest(parameters));

        return services;
    }

    private PhotoService getPhotoServiceForTest(final Parameters parameters) {
        final PhotoService photoService = EasyMock.createMock(PhotoService.class);

        EasyMock.expect(photoService.load(testData.getPhoto().getId())).andReturn(testData.getPhoto()).anyTimes();
        EasyMock.expect(photoService.getPhotosCountByUser(testData.getCommentAuthor().getId())).andReturn(parameters.getPhotosQty()).anyTimes();

        EasyMock.expectLastCall();
        EasyMock.replay(photoService);

        return photoService;
    }

    private class Parameters {

        private final User accessor;
        private final int photosQty;

        public Parameters(final User accessor, final int photosQty) {
            this.accessor = accessor;
            this.photosQty = photosQty;
        }

        public User getAccessor() {
            return accessor;
        }

        public int getPhotosQty() {
            return photosQty;
        }
    }
}
