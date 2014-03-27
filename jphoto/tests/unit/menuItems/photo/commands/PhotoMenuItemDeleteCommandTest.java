package menuItems.photo.commands;

import common.AbstractTestCase;
import core.general.menus.photo.commands.PhotoMenuItemDeleteCommand;
import core.general.photo.Photo;
import core.general.user.User;
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
		photo.setId( 444 );

		final User accessor = new User( 234 );
		accessor.setLanguage( AbstractTestCase.MENU_LANGUAGE );

		final PhotoMenuItemDeleteCommand command = new PhotoMenuItemDeleteCommand( photo, accessor, getServices() );
		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated( "Delete photo" ) );
		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format( "deletePhoto( %d ); return false;", photo.getId() ) );
	}
}
