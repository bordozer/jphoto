package core.general.menus.comment.items;

import core.general.menus.*;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class CommentAdminSubMenuItem extends AbstractCommentMenuItem {

	final List<EntryMenuOperationType> entryMenuOperationTypes = newArrayList(
		EntryMenuOperationType.ADMIN_MENU_ITEM_EDIT
		, EntryMenuOperationType.ADMIN_MENU_ITEM_DELETE
		, EntryMenuOperationType.SEPARATOR
		, EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER
		, EntryMenuOperationType.SEPARATOR
		, EntryMenuOperationType.ADMIN_SEND_PRIVATE_MESSAGE
	);

	public CommentAdminSubMenuItem( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.COMMENT_ADMIN_SUB_MENU;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {

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
	public boolean isAccessibleFor() {
		return getSecurityService().isSuperAdminUser( accessor.getId() ) && ! isCommentLeftByUserWhoIsCallingMenu();
	}

	public EntryMenu getEntrySubMenu() {
		return new EntryMenu( menuEntry, EntryMenuType.COMMENT, getSubMenus() );
	}

	@Override
	public String getMenuCssClass() {
		return ADMIN_MENU_ITEM_CSS_CLASS;
	}

	private List<? extends AbstractEntryMenuItem> getSubMenus() {
		return services.getEntryMenuService().getCommentMenu( menuEntry, accessor, entryMenuOperationTypes ).getEntryMenuItems();
	}
}
