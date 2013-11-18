package entryMenu.photo;

import core.general.menus.photo.items.PhotoMenuItemEdit;
import core.general.user.User;
import org.junit.Test;

public class PhotoMenuItemEditTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void menuIsInaccessibleToNotLoggedUser() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId(); // not logged user
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemEdit(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleToLoggedUser() {
		final int userWhoIsCallingMenuId = 11; // logged user
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemInaccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemEdit(), initialConditions );
	}

	/* = = = = */

	@Test
	public void menuIsAccessibleIfUserCanDeletePhoto() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );
		initialConditions.setUserCanEditPhoto( true ); // can edit

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemEdit(), initialConditions );
	}
}
