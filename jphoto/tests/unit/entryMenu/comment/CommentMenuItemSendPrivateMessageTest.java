package entryMenu.comment;

import core.general.user.User;
import core.general.menus.comment.items.CommentMenuItemSendPrivateMessage;
import org.junit.Test;

public class CommentMenuItemSendPrivateMessageTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void CommentMenuItemSendPrivateMessageIsInaccessibleToNotLoggedUser() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId();

		final int photoId = 123;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = 876;

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void CommentMenuItemSendPrivateMessageIsInaccessibleToCommentAuthor() {
		final int photoId = 123;
		final int userWhoIsCallingMenuId = 7;
		final int commentAuthorId = userWhoIsCallingMenuId; // the comment author is user who is calling menu
		final int photoCommentId = 1234;
		final int photoAuthorId = 876;

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void CommentMenuItemSendPrivateMessageIsInaccessibleIfMenuCallerInBlackListOfCommentAuthor() {
		final int photoId = 123;
		final int userWhoIsCallingMenuId = 7;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = 876;

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = true; // MenuCallerInBlackListOfCommentAuthor
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void CommentMenuItemSendPrivateMessageIsInaccessibleIfPhotoIsInAnonymousPeriod() {
		final int photoId = 123;
		final int userWhoIsCallingMenuId = 7;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = 876;

		final boolean isAnonymousPeriod = true; // isAnonymousPeriod!!!
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void CommentMenuItemSendPrivateMessageIsInaccessibleEvenForAdminForHisOwnComments() {
		final int photoId = 123;
		final int userWhoIsCallingMenuId = 7;
		final int commentAuthorId = userWhoIsCallingMenuId; // admin see own comment's menu
		final int photoCommentId = 1234;
		final int photoAuthorId = 876;

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = true;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemSendPrivateMessage(), initialConditions );
	}

	/* === */

	@Test
	public void CommentMenuItemSendPrivateMessageIsAccessibleToUsualUser() {
		final int userWhoIsCallingMenuId = 7;
		final int photoId = 123;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = 876;

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void CommentMenuItemSendPrivateMessageIsAccessibleToPhotoAuthor() {
		final int photoId = 123;
		final int userWhoIsCallingMenuId = 7;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = 876;

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void CommentMenuItemSendPrivateMessageIsAccessibleToPhotoAuthorIfPhotoIsInAnonymousPeriod() {
		final int photoId = 123;
		final int userWhoIsCallingMenuId = 7;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = userWhoIsCallingMenuId; // menu is being called by photo author

		final boolean isAnonymousPeriod = true; // Photo Is In Anonymous Period
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void CommentMenuItemSendPrivateMessageIsAccessibleToSuperAdmin() {
		final int photoId = 123;
		final int userWhoIsCallingMenuId = 7;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = 876;

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = true;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void CommentMenuItemSendPrivateMessageIsAccessibleToSuperAdminEvenIfHeIsInBlackList() {
		final int photoId = 123;
		final int userWhoIsCallingMenuId = 7;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = 876;

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = true; // TRUE
		final boolean isMenuCallerSuperAdmin = true;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemSendPrivateMessage(), initialConditions );
	}

	@Test
	public void CommentMenuItemSendPrivateMessageIsAccessibleToSuperAdminIfPhotoIsInAnonymousPeriod() {
		final int photoId = 123;
		final int userWhoIsCallingMenuId = 7;
		final int commentAuthorId = 234;
		final int photoCommentId = 1234;
		final int photoAuthorId = 876;

		final boolean isAnonymousPeriod = true; // Anonymous
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = true;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, new CommentMenuItemSendPrivateMessage(), initialConditions );
	}
}
