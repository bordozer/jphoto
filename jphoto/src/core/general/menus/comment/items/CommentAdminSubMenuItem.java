package core.general.menus.comment.items;

import core.general.menus.*;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import utils.TranslatorUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class CommentAdminSubMenuItem extends AbstractCommentMenuItem {

	public static final String BEAN_NAME = "commentAdminSubMenuItem";

	private EntryMenu entrySubMenu;

	final List<EntryMenuOperationType> entryMenuOperationTypes = newArrayList(
		EntryMenuOperationType.MENU_ITEM_DELETE
		, EntryMenuOperationType.SEPARATOR
		, EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER
	);

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.COMMENT_ADMIN_SUB_MENU;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int commentId, final User userWhoIsCallingMenu ) {

		entrySubMenu = new EntryMenu( 0, EntryMenuType.COMMENT, getSubMenus( commentId, userWhoIsCallingMenu ) ); // TODO: menu id?

		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate( "ADMIN" );
			}

			@Override
			public String getMenuCommand() {
				return "return false;";
			}
		};
	}

	@Override
	public boolean isAccessibleForComment( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		return securityService.isSuperAdminUser( userWhoIsCallingMenu.getId() ) && ! isCommentLeftByUserWhoIsCallingMenu( photoComment, userWhoIsCallingMenu );
	}

	public EntryMenu getEntrySubMenu() {
		return entrySubMenu;
	}

	private List<? extends AbstractEntryMenuItem> getSubMenus( final int commentId, final User userWhoIsCallingMenu ) {
		return entryMenuService.getCommentMenu( photoCommentService.load( commentId ), userWhoIsCallingMenu, entryMenuOperationTypes ).getEntryMenuItems();
	}
}
