package menuItems.photo.operation;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.operation.AbstractPhotoUserOperationsMenuItem;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbstractPhotoUserOperationsMenuItemTest extends AbstractPhotoMenuItemTest_ {

    @Test
    public void adminCanSeeOwnPhotoMenuTest() {

        final Photo photo = testData.getPhoto();
        photo.setUserId(SUPER_ADMIN_1.getId());

        final Parameters parameters = new Parameters(SUPER_ADMIN_1, true, photo);

        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, getMenuEntry(parameters).isAccessibleFor());
    }

    @Test
    public void photoAuthorCanSeeOwnPhotoMenuTest() {

        final Parameters parameters = new Parameters(testData.getPhotoAuthor(), true, testData.getPhoto());

        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, getMenuEntry(parameters).isAccessibleFor());
    }

    @Test
    public void userCanSeeMenuIfHeHasAccessToEditPhotoTest() {

        final Parameters parameters = new Parameters(testData.getPhotoAuthor(), true, testData.getPhoto());

        assertTrue(MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, getMenuEntry(parameters).isAccessibleFor());
    }

    @Test
    public void adminCanNotSeeMenuIfThisIsNOTHisPhotoTest() {

        final Parameters parameters = new Parameters(SUPER_ADMIN_1, true, testData.getPhoto());

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuEntry(parameters).isAccessibleFor());
    }

    @Test
    public void userCanNotSeeMenuIfHeIsNotPhotoAuthorAndDoesNotHaveAccessToEditPhotoTest() {

        final Parameters parameters = new Parameters(testData.getAccessor(), false, testData.getPhoto());

        assertFalse(MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, getMenuEntry(parameters).isAccessibleFor());
    }

    private AbstractPhotoUserOperationsMenuItem getMenuEntry(final Parameters parameters) {
        return new AbstractPhotoUserOperationsMenuItem(parameters.getPhoto(), parameters.getAccessor(), getServices(parameters.getAccessor())) {

            @Override
            public boolean hasAccessTo() {
                return parameters.isHasAccessToOperation();
            }

            @Override
            public EntryMenuOperationType getEntryMenuType() {
                return null;
            }

            @Override
            public AbstractEntryMenuItemCommand getMenuItemCommand() {
                return null;
            }
        };
    }

    private static class Parameters {

        private final User accessor;
        private final boolean hasAccessToOperation;
        private final Photo photo;

        private Parameters(final User accessor, final boolean hasAccessToOperation, final Photo photo) {
            this.accessor = accessor;
            this.hasAccessToOperation = hasAccessToOperation;
            this.photo = photo;
        }

        public User getAccessor() {
            return accessor;
        }

        public boolean isHasAccessToOperation() {
            return hasAccessToOperation;
        }

        public Photo getPhoto() {
            return photo;
        }
    }
}
