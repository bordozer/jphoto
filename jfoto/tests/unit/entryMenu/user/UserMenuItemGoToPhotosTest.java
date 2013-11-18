package entryMenu.user;

import core.general.menus.user.items.UserMenuItemGoToPhotos;
import org.junit.Test;

public class UserMenuItemGoToPhotosTest extends AbstractUserMenuItemTest_ {

	@Test
	public void ownMenuIsInaccessibleForLoggedUserIfItIsSwitchedOFFInSystemConfig() {
		final int userWhoIsCallingMenuId = 11;
		final int userId = userWhoIsCallingMenuId; // The same

		final UserInitialConditions initialConditions = new UserInitialConditions( userId, userWhoIsCallingMenuId );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( false );
		initialConditions.setUserPhotosQty( 1 );

		final UserMenuItemAccessStrategy accessStrategy = UserMenuItemAccessStrategy.getUserMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new UserMenuItemGoToPhotos(), initialConditions );
	}

	@Test
	public void ownMenuIsInaccessibleForLoggedUserIfPhotoQtyIsZero() {
		final int userWhoIsCallingMenuId = 11;
		final int userId = 33;

		final UserInitialConditions initialConditions = new UserInitialConditions( userId, userWhoIsCallingMenuId );
		initialConditions.setUserPhotosQty( 0 );

		final UserMenuItemAccessStrategy accessStrategy = UserMenuItemAccessStrategy.getUserMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new UserMenuItemGoToPhotos(), initialConditions );
	}

	/* === */

	@Test
	public void ownMenuIsAccessibleForLoggedUserIfItIsSwitchedONInSystemConfig() {
		final int userWhoIsCallingMenuId = 11;
		final int userId = userWhoIsCallingMenuId; // The same

		final UserInitialConditions initialConditions = new UserInitialConditions( userId, userWhoIsCallingMenuId );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( true );
		initialConditions.setUserPhotosQty( 1 );

		final UserMenuItemAccessStrategy accessStrategy = UserMenuItemAccessStrategy.getUserMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new UserMenuItemGoToPhotos(), initialConditions );
	}
}
