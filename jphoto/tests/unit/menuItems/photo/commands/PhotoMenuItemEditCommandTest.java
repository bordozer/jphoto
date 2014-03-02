package menuItems.photo.commands;

import common.AbstractTestCase;
import core.general.menus.photo.commands.PhotoMenuItemEditCommand;
import core.general.photo.Photo;
import core.services.security.ServicesImpl;
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
		photo.setId( 444 );

		final PhotoMenuItemEditCommand command = new PhotoMenuItemEditCommand( photo, new ServicesImpl() );
		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), "Edit photo" );
		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format( "editPhotoData( %d );", photo.getId() ) );
	}
}
