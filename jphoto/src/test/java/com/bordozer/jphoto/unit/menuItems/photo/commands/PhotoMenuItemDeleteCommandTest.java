package menuItems.photo.commands;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.commands.PhotoMenuItemDeleteCommand;
import com.bordozer.jphoto.unit.common.AbstractTestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoMenuItemDeleteCommandTest extends AbstractTestCase {

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void commandTest() {
        final Photo photo = new Photo();
        photo.setId(444);

        final User accessor = new User(234);
        accessor.setLanguage(AbstractTestCase.MENU_LANGUAGE);

        final PhotoMenuItemDeleteCommand command = new PhotoMenuItemDeleteCommand(photo, accessor, getServices());
        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated("PhotoMenuItem: Delete photo"));
        assertEquals(EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), "deletePhotoFromContextMenu();");
    }
}
