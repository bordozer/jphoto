package entryMenu.comment;

import core.general.user.User;
import core.general.menus.comment.items.CommentMenuItemGoToAuthorPhotoByGenre;
import org.junit.Test;

public class CommentMenuItemGoToAuthorPhotoByGenreTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void menuIsInaccessibleIfCommentAuthorOwnerOfPhotoAndHasOneOrLessPhotoInGenre() {
		final int userWhoIsCallingMenuId = 7;
		final int photoCommentId = 1234;
		final int commentAuthorId = 234;
		final int photoId = 123;
		final int photoAuthorId = commentAuthorId; // comment author is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		initialConditions.setPhotoCommentAuthorPhotosQty( 1 ); // Has only one photo in genre

		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}

	@Test
	public void menuIsInAccessibleIfCommentAuthorIsNotOwnerOfPhotoAndHasNoPhotoInGenre() {
		final int userWhoIsCallingMenuId = 7;
		final int photoCommentId = 1234;
		final int commentAuthorId = 234;
		final int photoId = 123;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		initialConditions.setPhotoCommentAuthorPhotosQty( 0 ); // Has no photo in genre

		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}

	@Test
	public void menuIsInAccessibleIfPhotoIsInAnonymousPeriod() {
		final int userWhoIsCallingMenuId = 7;
		final int photoCommentId = 1234;
		final int commentAuthorId = 234;
		final int photoId = 123;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = true; // Anonymous period
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		initialConditions.setPhotoCommentAuthorPhotosQty( 10 ); // photos qty does not matter

		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}

	@Test
	public void menuIsInAccessibleIfCommentOwnerIsCallingItAndShowOwnEntriesIsSwitchedOff() {
		checkIfMenuAccessibleIfCommentOwnerIsCallingItDependsFromShowOwnEntriesMenuSetting( false, CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy() );
	}

	/* =============================================================================================================================================================================== */

	@Test
	public void menuIsInAccessibleIfCommentOwnerIsCallingItAndShowOwnEntriesIsSwitchedOn() {
		checkIfMenuAccessibleIfCommentOwnerIsCallingItDependsFromShowOwnEntriesMenuSetting( true, CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy() );
	}

	@Test
	public void menuIsAccessibleIfNOTCommentOwnerIsCallingItEvenIfShowOwnEntriesConfigurationIsSwitchedOff() {
		final int userWhoIsCallingMenuId = 7;
		final int photoCommentId = 1234;
		final int commentAuthorId = 234; // menu is being called not by comment author
		final int photoId = 123;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		initialConditions.setPhotoCommentAuthorPhotosQty( 10 ); // photos qty does not matter
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( false );

		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}

	@Test
	public void menuIsAccessibleToNotLoggedUser() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId();
		final int photoCommentId = 1234;
		final int commentAuthorId = 234;
		final int photoId = 123;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		initialConditions.setPhotoCommentAuthorPhotosQty( 2 );

		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}

	@Test
	public void menuIsAccessibleIfCommentAuthorOwnerOfPhotoAndHasMoreThenOnePhotoInGenre() {
		final int userWhoIsCallingMenuId = 7;
		final int photoCommentId = 1234;
		final int commentAuthorId = 234;
		final int photoId = 123;
		final int photoAuthorId = userWhoIsCallingMenuId; // photo owner is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		initialConditions.setPhotoCommentAuthorPhotosQty( 2 ); // Has more then one photo in genre

		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}

	@Test
	public void menuIsAccessibleIfCommentAuthorIsNotOwnerOfPhotoAndHasOneOrMorePhotoInGenre() {
		final int userWhoIsCallingMenuId = 7;
		final int photoCommentId = 1234;
		final int commentAuthorId = 234;
		final int photoId = 123;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		initialConditions.setPhotoCommentAuthorPhotosQty( 1 ); // Has one or more photo(s) in genre

		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}

	private void checkIfMenuAccessibleIfCommentOwnerIsCallingItDependsFromShowOwnEntriesMenuSetting( final boolean isShowMenuForOwnEntriesConfigurationIsSwitchedOn, final CommentMenuItemAccessStrategy strategy ) {
		final int userWhoIsCallingMenuId = 7;
		final int photoCommentId = 1234;
		final int commentAuthorId = userWhoIsCallingMenuId; // comment author is calling menu
		final int photoId = 123;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		initialConditions.setPhotoCommentAuthorPhotosQty( 10 ); // photos qty does not matter
		initialConditions.setShowMenuGoToForOwnEntriesSettingIsSwitchedOn( isShowMenuForOwnEntriesConfigurationIsSwitchedOn );

		assertCommentMenuItemAccess( strategy, new CommentMenuItemGoToAuthorPhotoByGenre(), initialConditions );
	}
}
