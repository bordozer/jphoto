package entryMenu.comment;

import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.items.AbstractCommentComplaintMenuItem;
import org.junit.Test;

public class CommentMenuItemComplaintAbstractTest extends AbstractCommentMenuItemTest_ {

	public static final AbstractCommentComplaintMenuItem COMMENT_MENU_ITEM_ABSTRACT_COMPLAINT = new AbstractCommentComplaintMenuItem() {

		@Override
		public EntryMenuOperationType getEntryMenuType() {
			return null;
		}

		@Override
		protected AbstractEntryMenuItemCommand initMenuItemCommand( final int commentId, final User userWhoIsCallingMenu ) {
			return null;
		}
	};

	@Test
	public void menuIsAccessibleForAnyLoggedUser() {
		final int userWhoIsCallingMenuId = 7;
		final int photoCommentId = 1234;
		final int photoId = 123;
		final int commentAuthorId = 9;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemAccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, COMMENT_MENU_ITEM_ABSTRACT_COMPLAINT, initialConditions );
	}

	@Test
	public void menuIsInaccessibleForNotLoggedUser() {
		final int userWhoIsCallingMenuId = User.NOT_LOGGED_USER.getId(); // Not logged user
		final int photoCommentId = 1234;
		final int photoId = 123;
		final int commentAuthorId = 9;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, COMMENT_MENU_ITEM_ABSTRACT_COMPLAINT, initialConditions );
	}

	@Test
	public void menuIsInaccessibleForOwnComments() {
		final int userWhoIsCallingMenuId = 7;
		final int photoCommentId = 1234;
		final int photoId = 123;
		final int commentAuthorId = userWhoIsCallingMenuId; // Comment author and menu caller the same
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = false;

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, COMMENT_MENU_ITEM_ABSTRACT_COMPLAINT, initialConditions );
	}

	@Test
	public void menuIsInaccessibleForSuperAdmins() {
		final int userWhoIsCallingMenuId = 7;
		final int photoCommentId = 1234;
		final int photoId = 123;
		final int commentAuthorId = 543;
		final int photoAuthorId = 876; // any user but not who is calling menu

		final boolean isAnonymousPeriod = false;
		final boolean isMenuCallerInBlackListOfCommentAuthor = false;
		final boolean isMenuCallerSuperAdmin = true; // Super admin

		final CommentInitialConditions initialConditions = new CommentInitialConditions( userWhoIsCallingMenuId, photoId, photoCommentId, commentAuthorId, photoAuthorId, isAnonymousPeriod, isMenuCallerInBlackListOfCommentAuthor, isMenuCallerSuperAdmin );
		final CommentMenuItemAccessStrategy accessStrategy = CommentMenuItemAccessStrategy.getCommentMenuItemInaccessibleStrategy();

		assertCommentMenuItemAccess( accessStrategy, COMMENT_MENU_ITEM_ABSTRACT_COMPLAINT, initialConditions );
	}
}
