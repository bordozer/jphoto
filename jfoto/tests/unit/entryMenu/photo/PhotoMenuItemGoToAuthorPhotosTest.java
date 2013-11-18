package entryMenu.photo;

import core.general.menus.photo.items.PhotoMenuItemGoToAuthorPhotos;
import core.general.user.User;
import org.junit.Test;

public class PhotoMenuItemGoToAuthorPhotosTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void menuIsInaccessibleIfThereIsOnlyOnePhoto() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId();
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 1 );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertAccess( initialConditions, accessStrategy );
	}

	@Test
	public void menuIsInaccessibleIfShowMenuGoToForOwnEntriesSettingIsSwitchedOff() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = userWhoIsCallingMenuId; // the album of menu caller

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 2 );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( false ); // OFF

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertAccess( initialConditions, accessStrategy );
	}

	@Test
	public void menuIsInaccessibleIfAnonymousPeriod() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 2 );
		initialConditions.setPhotoAuthorNameMustBeHidden( true ); // the author is hidden

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertAccess( initialConditions, accessStrategy );
	}

	@Test
	public void menuIsInaccessibleForSuperAdminIfOnlyOnePhoto() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 1 ); // One photo
		initialConditions.setMenuCallerSuperAdmin( true ); // admin

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertAccess( initialConditions, accessStrategy );
	}

	/* = = = = */

	@Test
	public void menuIsAccessibleIfThereIsMoreThenOnePhoto() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId();
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 2 ); // more then one photos

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertAccess( initialConditions, accessStrategy );
	}

	@Test
	public void menuIsAccessibleIfShowMenuGoToForOwnEntriesSettingIsSwitchedOn() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = userWhoIsCallingMenuId; // the photo of menu caller

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 2 );
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( true ); // ON

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertAccess( initialConditions, accessStrategy );
	}

	@Test
	public void menuIsAccessibleForSuperAdmin() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 2 );
		initialConditions.setMenuCallerSuperAdmin( true ); // admin

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertAccess( initialConditions, accessStrategy );
	}

	@Test
	public void menuIsAccessibleForSuperAdminEvenIfAnonymousPeriod() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 2 );
		initialConditions.setMenuCallerSuperAdmin( true ); // admin
		initialConditions.setPhotoAuthorNameMustBeHidden( true ); // the author is hidden

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertAccess( initialConditions, accessStrategy );
	}

	private void assertAccess( final PhotoInitialConditions initialConditions, final PhotoMenuItemAccessStrategy accessStrategy ) {
		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemGoToAuthorPhotos(), initialConditions );
	}
}
