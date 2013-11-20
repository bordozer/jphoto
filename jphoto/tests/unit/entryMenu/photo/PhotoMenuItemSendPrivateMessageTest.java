package entryMenu.photo;

import core.general.user.User;
import core.general.menus.photo.items.PhotoMenuItemSendPrivateMessage;
import org.junit.Test;

public class PhotoMenuItemSendPrivateMessageTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void menuIsInaccessibleForNotLoggedUser() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId();
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleForUserWhoIsInBlackListOfPhotoAuthor() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( true );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleForPhotoAuthorIsShowMenuForOwnEntriesConfigurationIsSwitchedOFF() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = userWhoIsCallingMenuId; // user is calling menu is photo author

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( false );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleEvenForSuperAdminIfShowMenuForOwnEntriesConfigurationIsSwitchedOFF() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = userWhoIsCallingMenuId; // Admin is seeing it's own menu

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( false ); // ... but it is switched off
		initialConditions.setMenuCallerSuperAdmin( true );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleForUsualUserIfPhotoIsInAnonymousPeriod() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( false );
		initialConditions.setMenuCallerSuperAdmin( false );
		initialConditions.setPhotoAuthorNameMustBeHidden( true );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemSendPrivateMessage(), initialConditions );
	}

	/* === */

	@Test
	public void menuIsAccessibleForLoggedUserAndNotInBlackList() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void menuIsAccessibleForPhotoAuthorIsShowMenuForOwnEntriesConfigurationIsSwitchedON() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = userWhoIsCallingMenuId; // user is calling menu is photo author

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( true );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void menuIsAccessibleForSuperAdmin() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( false );
		initialConditions.setMenuCallerSuperAdmin( true );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void menuIsAccessibleForAdminIfPhotosInAnonymousPeriod() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( false );
		initialConditions.setMenuCallerSuperAdmin( true ); // Admin
		initialConditions.setPhotoAuthorNameMustBeHidden( true ); // Author name should be hidden

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void menuIsAccessibleForAdminEvenIfHeIsInBlackList() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( true ); // TRUE
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( false );
		initialConditions.setMenuCallerSuperAdmin( true ); // Admin
		initialConditions.setPhotoAuthorNameMustBeHidden( false );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemSendPrivateMessage(), initialConditions );
	}
}
