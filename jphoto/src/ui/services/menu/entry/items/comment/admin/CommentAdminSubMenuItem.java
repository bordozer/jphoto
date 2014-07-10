package ui.services.menu.entry.items.comment.admin;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.*;
import ui.services.menu.entry.items.comment.AbstractCommentMenuItem;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class CommentAdminSubMenuItem extends AbstractCommentMenuItem implements SubmenuAccesible {

	private final List<EntryMenuData> entryMenuOperationTypes = newArrayList(
		new EntryMenuData( EntryMenuOperationType.ADMIN_MENU_ITEM_EDIT )
		, new EntryMenuData( EntryMenuOperationType.ADMIN_MENU_ITEM_DELETE )
		, new EntryMenuData( EntryMenuOperationType.SEPARATOR )
		, new EntryMenuData( EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER )
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

		return new AbstractEntryMenuItemCommand<PhotoComment>( menuEntry, accessor, services ) {
			@Override
			public String getMenuText() {
				return getTranslatorService().translate( ADMIN_SUB_MENU_ENTRY_TEXT, getLanguage() );
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
		return new EntryMenu( menuEntry, EntryMenuType.COMMENT, getSubMenus(), getLanguage(), services );
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_ADMIN_CSS_CLASS;
	}

	private List<? extends AbstractEntryMenuItem> getSubMenus() {
		return services.getEntryMenuService().getCommentMenu( menuEntry, accessor, entryMenuOperationTypes ).getEntryMenuItems();
	}
}
