package entryMenu.photo;

import core.general.menus.photo.items.PhotoMenuItemGoToAuthorPhotoByGenre;
import core.general.user.User;
import org.junit.Test;

public class PhotoMenuItemGoToAuthorPhotoByGenreTest extends AbstractPhotoMenuItemTest_  {

	@Test
	public void menuIsInaccessibleIfThereIsOnlyOnePhotoInGenre() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId();
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoQtyByUserAndGenre( 1 );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleIfShowMenuGoToForOwnEntriesSettingIsSwitchedOff() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = userWhoIsCallingMenuId; // the album of menu caller

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoQtyByUserAndGenre( 2 );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( false ); // OFF

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleIfAnonymousPeriod() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoQtyByUserAndGenre( 2 );
		initialConditions.setPhotoAuthorNameMustBeHidden( true ); // the author is hidden

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleForSuperAdminIfOnlyOnePhotoInGenre() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoQtyByUserAndGenre( 1 ); // One photo
		initialConditions.setMenuCallerSuperAdmin( true ); // admin

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}

	/* = = = = */

	@Test
	public void menuIsAccessibleIfThereIsMoreThenOnePhotoInGenre() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId();
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoQtyByUserAndGenre( 2 ); // more then one photos

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}

	@Test
	public void menuIsAccessibleIfShowMenuGoToForOwnEntriesSettingIsSwitchedOn() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = userWhoIsCallingMenuId; // the photo of menu caller

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoQtyByUserAndGenre( 2 );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( true ); // ON

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}

	@Test
	public void menuIsAccessibleForSuperAdmin() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoQtyByUserAndGenre( 2 );
		initialConditions.setMenuCallerSuperAdmin( true ); // admin

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}

	@Test
	public void menuIsAccessibleForSuperAdminEvenIfAnonymousPeriod() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoQtyByUserAndGenre( 2 );
		initialConditions.setMenuCallerSuperAdmin( true ); // admin
		initialConditions.setPhotoAuthorNameMustBeHidden( true ); // the author is hidden

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}
}
