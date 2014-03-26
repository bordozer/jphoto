package core.general.menus.comment.admin;

import core.general.menus.*;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class CommentAdminSubMenuItem extends AbstractCommentMenuItem {

	private final List<EntryMenuOperationType> entryMenuOperationTypes = newArrayList(
		EntryMenuOperationType.ADMIN_MENU_ITEM_EDIT
		, EntryMenuOperationType.ADMIN_MENU_ITEM_DELETE
		, EntryMenuOperationType.SEPARATOR
		, EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER
	);

	public CommentAdminSubMenuItem( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_SUB_MENU;
	}

	@Override
	public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {

		return new AbstractEntryMenuItemCommand<PhotoComment>( menuEntry, services ) {
			@Override
			public String getMenuText() {
				return getTranslatorService().translate( ADMIN_SUB_MENU_ENTRY_TEXT, accessor.getLanguage() );
			}

			@Override
			public String getMenuCommand() {
				return ADMIN_SUB_MENU_ENTRY_COMMAND;
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {

		if ( ! isAccessorSuperAdmin() ) {
			return false;
		}

		return ! isCommentLeftByAccessor();
	}

	public EntryMenu getEntrySubMenu() {
		return new EntryMenu( menuEntry, EntryMenuType.COMMENT, getSubMenus(), accessor.getLanguage(), services );
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_CSS_CLASS_ADMIN;
	}

	private List<? extends AbstractEntryMenuItem> getSubMenus() {
		return services.getEntryMenuService().getCommentMenu( menuEntry, accessor, entryMenuOperationTypes ).getEntryMenuItems();
	}
}
