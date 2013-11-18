package entryMenu.comment;

import core.general.user.User;
import core.general.menus.comment.items.CommentMenuItemDelete;
import org.junit.Test;

public class CommentMenuItemDeleteTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void menuIsInaccessibleToNotLoggedUser() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId(); // Not logged
		final int photoId = 123;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemDelete(), initialConditions );
	}

	public void menuIsInaccessibleToUsualUser() {
		final int userWhoIsCallingMenuId = 7;
		final int photoId = 123;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemDelete(), initialConditions );
	}

	@Test
	public void menuIsAccessibleToSuperAdmin() {
		final int userWhoIsCallingMenuId = 7;
		final int photoId = 123;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = true; // Super admin

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemDelete(), initialConditions );
	}

	@Test
	public void menuIsAccessibleToPhotoAuthor() {
		final int userWhoIsCallingMenuId = 7;
		final int photoId = 123;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = userWhoIsCallingMenuId; // photo author is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemDelete(), initialConditions );
	}

	@Test
	public void menuIsAccessibleToCommentAuthor() {
		final int userWhoIsCallingMenuId = 7;
		final int photoId = 123;
		final int commentAuthorId = userWhoIsCallingMenuId; // Comment author is calling menu
		final int photoCommentId = 1234;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemDelete(), initialConditions );
	}
}
