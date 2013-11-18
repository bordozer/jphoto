package entryMenu.comment;

import core.general.menus.comment.AbstractCommentMenuItem;
import entryMenu.AbstractEntryMenuItemAccessStrategy;
import entryMenu.Initialable;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

abstract class CommentMenuItemAccessStrategy extends AbstractEntryMenuItemAccessStrategy<AbstractCommentMenuItem> {

	public static CommentMenuItemAccessStrategy getCommentMenuItemInaccessibleStrategy() {

		return new CommentMenuItemAccessStrategy() {
			@Override
			public void assertMenuItemAccess( final AbstractCommentMenuItem menuItem, final Initialable initialConditions ) {
				final CommentInitialConditions conditions = ( CommentInitialConditions ) initialConditions;
				assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, menuItem.isAccessibleForComment( conditions.getPhotoComment(), conditions.getUserWhoIsCallingMenu() ) );
			}
		};
	}

	public static CommentMenuItemAccessStrategy getCommentMenuItemAccessibleStrategy() {

		return new CommentMenuItemAccessStrategy() {
			@Override
			public void assertMenuItemAccess( final AbstractCommentMenuItem menuItem, final Initialable initialConditions ) {
				final CommentInitialConditions conditions = ( CommentInitialConditions ) initialConditions;
				assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, menuItem.isAccessibleForComment( conditions.getPhotoComment(), conditions.getUserWhoIsCallingMenu() ) );
			}
		};
	}
}
