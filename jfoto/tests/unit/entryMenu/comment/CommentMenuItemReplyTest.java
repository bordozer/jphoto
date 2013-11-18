package entryMenu.comment;

import core.general.user.User;
import core.general.menus.comment.items.CommentMenuItemReply;
import org.junit.Test;

public class CommentMenuItemReplyTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void menuIsInaccessibleToNotLoggedUser() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId();
		final int photoCommentId = 1234;
		final int photoId = 123;
		final int commentAuthorId = 234;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemReply(), initialConditions );
	}

	@Test
	public void menuIsInaccessibleToCommentAuthor() {
		final int photoId = 123;
		final int userWhoIsCallingMenuId = 7;
		final int commentAuthorId = userWhoIsCallingMenuId; // the comment author is user who is calling menu
		final int photoCommentId = 1234;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemReply(), initialConditions );
	}

	@Test
	public void menuIsAccessibleToUsualUser() {
		final int photoId = 123;
		final int userWhoIsCallingMenuId = 7;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemReply(), initialConditions );
	}

	@Test
	public void menuIsAccessibleToSuperAdmin() {
		final int photoId = 123;
		final int userWhoIsCallingMenuId = 7;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = true; // Super admin

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemReply(), initialConditions );
	}

	@Test
	public void menuIsAccessibleToPhotoAuthor() {
		final int photoId = 123;
		final int userWhoIsCallingMenuId = 7;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = userWhoIsCallingMenuId; // photo author is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemReply(), initialConditions );
	}
}
