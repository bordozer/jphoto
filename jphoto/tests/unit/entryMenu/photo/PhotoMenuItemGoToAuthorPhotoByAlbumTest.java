package entryMenu.photo;

import core.general.menus.photo.items.PhotoMenuItemGoToAuthorPhotoByAlbum;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import org.junit.Test;

public class PhotoMenuItemGoToAuthorPhotoByAlbumTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void menuIsInaccessibleIfThereIsOnlyOnePhotoInAlbum() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId();
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setUserPhotoAlbumPhotosQty( 1 ); // ONE photo

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		final UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
		userPhotoAlbum.setId( 2345 );

		final PhotoMenuItemGoToAuthorPhotoByAlbum menuItem = new PhotoMenuItemGoToAuthorPhotoByAlbum();
		menuItem.setUserPhotoAlbum( userPhotoAlbum );

		assertUserMenuItemAccess( accessStrategy, menuItem, initialConditions );
	}

	@Test
	public void menuIsInaccessibleIfShowMenuGoToForOwnEntriesSettingIsSwitchedOff() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = userWhoIsCallingMenuId; // the album of menu caller

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setUserPhotoAlbumPhotosQty( 10 );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( false ); // OFF

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		final UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
		userPhotoAlbum.setId( 2345 );

		final PhotoMenuItemGoToAuthorPhotoByAlbum menuItem = new PhotoMenuItemGoToAuthorPhotoByAlbum();
		menuItem.setUserPhotoAlbum( userPhotoAlbum );

		assertUserMenuItemAccess( accessStrategy, menuItem, initialConditions );
	}

	@Test
	public void menuIsInaccessibleIfAnonymousPeriod() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setUserPhotoAlbumPhotosQty( 10 );
		initialConditions.setPhotoAuthorNameMustBeHidden( true ); // the author is hidden

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		final UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
		userPhotoAlbum.setId( 2345 );

		final PhotoMenuItemGoToAuthorPhotoByAlbum menuItem = new PhotoMenuItemGoToAuthorPhotoByAlbum();
		menuItem.setUserPhotoAlbum( userPhotoAlbum );

		assertUserMenuItemAccess( accessStrategy, menuItem, initialConditions );
	}

	@Test
	public void menuIsInaccessibleForSuperAdminIfOnlyOnePhotoInAlbum() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setUserPhotoAlbumPhotosQty( 1 ); // One photo
		initialConditions.setMenuCallerSuperAdmin( true ); // admin

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		final UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
		userPhotoAlbum.setId( 2345 );

		final PhotoMenuItemGoToAuthorPhotoByAlbum menuItem = new PhotoMenuItemGoToAuthorPhotoByAlbum();
		menuItem.setUserPhotoAlbum( userPhotoAlbum );

		assertUserMenuItemAccess( accessStrategy, menuItem, initialConditions );
	}

	/* = = = = */

	@Test
	public void menuIsAccessibleIfThereIsMoreThenOnePhotoInAlbum() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId();
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setUserPhotoAlbumPhotosQty( 2 ); // more then one photos

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		final UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
		userPhotoAlbum.setId( 2345 );

		final PhotoMenuItemGoToAuthorPhotoByAlbum menuItem = new PhotoMenuItemGoToAuthorPhotoByAlbum();
		menuItem.setUserPhotoAlbum( userPhotoAlbum );

		assertUserMenuItemAccess( accessStrategy, menuItem, initialConditions );
	}

	@Test
	public void menuIsAccessibleIfShowMenuGoToForOwnEntriesSettingIsSwitchedOn() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = userWhoIsCallingMenuId; // the album of menu caller

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setUserPhotoAlbumPhotosQty( 10 );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( true ); // ON

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		final UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
		userPhotoAlbum.setId( 2345 );

		final PhotoMenuItemGoToAuthorPhotoByAlbum menuItem = new PhotoMenuItemGoToAuthorPhotoByAlbum();
		menuItem.setUserPhotoAlbum( userPhotoAlbum );

		assertUserMenuItemAccess( accessStrategy, menuItem, initialConditions );
	}

	@Test
	public void menuIsAccessibleForSuperAdmin() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setUserPhotoAlbumPhotosQty( 10 );
		initialConditions.setMenuCallerSuperAdmin( true ); // admin

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		final UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
		userPhotoAlbum.setId( 2345 );

		final PhotoMenuItemGoToAuthorPhotoByAlbum menuItem = new PhotoMenuItemGoToAuthorPhotoByAlbum();
		menuItem.setUserPhotoAlbum( userPhotoAlbum );

		assertUserMenuItemAccess( accessStrategy, menuItem, initialConditions );
	}

	@Test
	public void menuIsAccessibleForSuperAdminEvenIfAnonymousPeriod() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setUserPhotoAlbumPhotosQty( 10 );
		initialConditions.setMenuCallerSuperAdmin( true ); // admin
		initialConditions.setPhotoAuthorNameMustBeHidden( true ); // the author is hidden

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		final UserPhotoAlbum userPhotoAlbum = new UserPhotoAlbum();
		userPhotoAlbum.setId( 2345 );

		final PhotoMenuItemGoToAuthorPhotoByAlbum menuItem = new PhotoMenuItemGoToAuthorPhotoByAlbum();
		menuItem.setUserPhotoAlbum( userPhotoAlbum );

		assertUserMenuItemAccess( accessStrategy, menuItem, initialConditions );
	}
}
