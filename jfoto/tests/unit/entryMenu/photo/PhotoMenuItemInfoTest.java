package entryMenu.photo;

import core.general.menus.photo.items.PhotoMenuItemInfo;
import core.general.user.User;
import org.junit.Test;

public class PhotoMenuItemInfoTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void menuIsAccessibleToNotLoggedUser() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId(); // not logged user
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemInfo(), initialConditions );
	}

	@Test
	public void menuIsAccessibleToLoggedUser() {
		final int userWhoIsCallingMenuId = 11; // logged user
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemInfo(), initialConditions );
	}

	@Test
	public void menuIsAccessibleForAnonymousPhoto() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( false );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );
		initialConditions.setPhotoAuthorNameMustBeHidden( true ); // anonymous posting

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemInfo(), initialConditions );
	}

	@Test
	public void menuIsAccessibleForBlackListUser() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = 22;

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( true ); // black list
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemInfo(), initialConditions );
	}

	@Test
	public void menuIsAccessibleForPhotoAuthor() {
		final int userWhoIsCallingMenuId = 11;
		final int photoId = 33;
		final int photoAuthorId = userWhoIsCallingMenuId; // The author is calling menu

		final PhotoInitialConditions initialConditions = new PhotoInitialConditions( userWhoIsCallingMenuId, photoId );
		initialConditions.setUserWhoIsCallingMenuInTheBlackList( true );
		initialConditions.setPhotoAuthorId( photoAuthorId );
		initialConditions.setPhotoAuthorPhotosQty( 111 );

		final PhotoMenuItemAccessStrategy accessStrategy = PhotoMenuItemAccessStrategy.getPhotoMenuItemAccessibleStrategy();

		assertUserMenuItemAccess( accessStrategy, new PhotoMenuItemInfo(), initialConditions );
	}
}
