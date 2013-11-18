package entryMenu.comment;

import core.general.user.User;
import core.general.menus.comment.items.CommentMenuItemEdit;
import org.junit.Test;

public class CommentMenuItemEditTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void menuIsInaccessibleToNotLoggedUser() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId(); // Not logged
		final int photoCommentId = 1234;
		final int photoId = 123;
		final int commentAuthorId = 234;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemEdit(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleToUsualUser() {
		final int userWhoIsCallingMenuId = 7;
		final int photoCommentId = 1234;
		final int photoId = 123;
		final int commentAuthorId = 234;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemEdit(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleToSuperAdmin() {
		final int userWhoIsCallingMenuId = 7;
		final int photoCommentId = 1234;
		final int photoId = 123;
		final int commentAuthorId = 234;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemEdit(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleToPhotoAuthor() {
		final int userWhoIsCallingMenuId = 7;
		final int photoCommentId = 1234;
		final int photoId = 123;
		final int commentAuthorId = 234;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemEdit(), initialConditions );
	}

	@Test
	public void menuIsAccessibleToCommentAuthor() {
		final int userWhoIsCallingMenuId = 7;
		final int photoCommentId = 1234;
		final int photoId = 123;
		final int commentAuthorId = userWhoIsCallingMenuId; // the comment author is user who is calling menu
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemEdit(), initialConditions );
	}
}
