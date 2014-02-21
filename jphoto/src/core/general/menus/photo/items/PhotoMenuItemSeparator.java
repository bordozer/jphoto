package core.general.menus.photo.items;

import core.general.photo.Photo;
import core.general.user.User;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.photo.AbstractPhotoMenuItem;
import org.apache.commons.lang.StringUtils;

public class PhotoMenuItemSeparator extends AbstractPhotoMenuItem {

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.SEPARATOR;
	}

	@Override
	protected AbstractEntryMenuItemCommand initMenuItemCommand( final int photoId, final User userWhoIsCallingMenu ) {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {
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
	public boolean isAccessibleForPhoto( final Photo photo, final User userWhoIsCallingMenu ) {
		return true;
	}

	@Override
	public int getHeight() {
		return MENU_SEPARATOR_HEIGHT;
	}
}
