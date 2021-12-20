package menuItems.photo.commands;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.commands.PhotoMenuItemEditCommand;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoMenuItemEditCommandTest extends AbstractTestCase {

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void commandTest() {
        final Photo photo = new Photo();
        photo.setId(444);

        final User accessor = new User(345);
        accessor.setLanguage(AbstractTestCase.MENU_LANGUAGE);

        final PhotoMenuItemEditCommand command = new PhotoMenuItemEditCommand(photo, accessor, getServices());
        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated("PhotoMenuItem: Edit photo"));
        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format("editPhotoData( %d );", photo.getId()));
    }
}
