package entryMenu.user;

import core.general.user.User;
import core.general.menus.user.items.UserMenuItemSendPrivateMessage;
import org.junit.Test;

public class UserMenuItemSendPrivateMessageTest extends AbstractUserMenuItemTest_ {

	@Test
	public void menuIsInaccessibleToNotLoggedUser() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId();
		final int userId = 33;

		final UserInitialConditions initialConditions = new UserInitialConditions( userId, userWhoIsCallingMenuId );
		initialConditions.setUserPhotosQty( 111 );

		final UserMenuItemAccessStrategy accessStrategy = UserMenuItemAccessStrategy.getUserMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new UserMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleToUserFromBlackList() {
		final int userWhoIsCallingMenuId = 11;
		final int userId = 33;

		final UserInitialConditions initialConditions = new UserInitialConditions( userId, userWhoIsCallingMenuId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( true );
		initialConditions.setUserPhotosQty( 111 );

		final UserMenuItemAccessStrategy accessStrategy = UserMenuItemAccessStrategy.getUserMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new UserMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleToOwnMenu() {
		final int userWhoIsCallingMenuId = 11;
		final int userId = userWhoIsCallingMenuId; // The same

		final UserInitialConditions initialConditions = new UserInitialConditions( userId, userWhoIsCallingMenuId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( true );
		initialConditions.setUserPhotosQty( 111 );

		final UserMenuItemAccessStrategy accessStrategy = UserMenuItemAccessStrategy.getUserMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new UserMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleToOwnMenuEvenFoeAdmins() {
		final int userWhoIsCallingMenuId = 7;
		final int userId = userWhoIsCallingMenuId; // Admin is seeing his own menu

		final UserInitialConditions initialConditions = new UserInitialConditions( userId, userWhoIsCallingMenuId );
		initialConditions.setUserPhotosQty( 111 );
		initialConditions.setMenuCallerSuperAdmin( true ); // Admin

		final UserMenuItemAccessStrategy accessStrategy = UserMenuItemAccessStrategy.getUserMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new UserMenuItemSendPrivateMessage(), initialConditions );
	}

	/* === */

	@Test
	public void menuIsAccessibleToLoggedUserAndNotInBlackList() {
		final int userWhoIsCallingMenuId = 11;
		final int userId = 33;

		final UserInitialConditions initialConditions = new UserInitialConditions( userId, userWhoIsCallingMenuId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setUserPhotosQty( 111 );

		final UserMenuItemAccessStrategy accessStrategy = UserMenuItemAccessStrategy.getUserMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new UserMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void menuIsAccessibleToSuperAdminEvenIfHeIsInBlackList() {
		final int userWhoIsCallingMenuId = 11;
		final int userId = 33;

		final UserInitialConditions initialConditions = new UserInitialConditions( userId, userWhoIsCallingMenuId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( true ); // black list
		initialConditions.setMenuCallerSuperAdmin( true ); // Admin
		initialConditions.setUserPhotosQty( 111 );

		final UserMenuItemAccessStrategy accessStrategy = UserMenuItemAccessStrategy.getUserMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new UserMenuItemSendPrivateMessage(), initialConditions );
	}
}
