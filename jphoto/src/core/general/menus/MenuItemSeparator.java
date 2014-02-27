package core.general.menus;

import core.general.user.User;
import core.services.security.Services;
import org.apache.commons.lang.StringUtils;

public class MenuItemSeparator<T extends PopupMenuAssignable> extends AbstractEntryMenuItem<T> {

	public static final String BEAN_NAME = "separatorMenuItem";

	public MenuItemSeparator( final T menuEntry, final User accessor, final Services services ) {
		super( menuEntry, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.SEPARATOR;
	}

	@Override
	public boolean isAccessibleFor() {
		return true;
	}

	@Override
	public AbstractEntryMenuItemCommand<T> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<T>( menuEntry ) {
			@Override
			public String getMenuText() {
				return StringUtils.EMPTY;
			}

			@Override
			public String getMenuCommand() {
				return StringUtils.EMPTY;
			}
		};
	}

	@Override
	public int getHeight() {
		return MENU_SEPARATOR_HEIGHT;
	}
}
