package core.services.menu;

import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.PopupMenuAssignable;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;

import java.util.List;

public class MenuCreationFactory<T extends PopupMenuAssignable> {

	List<AbstractEntryMenuItem<T>> getAllMenuEntries( final T menuEntry, final User accessor, final EntryMenuOperationType entryMenuOperationType, final Services services ) {

		if ( menuEntry instanceof PhotoComment ) {
			final List<AbstractEntryMenuItem<PhotoComment>> menu = MenuCreationStrategy.COMMENT_MENU_CREATION_STRATEGY.getMenuEntries( ( PhotoComment ) menuEntry, accessor, entryMenuOperationType, services );
			return menu;
		}

		if ( menuEntry instanceof Photo ) {
			return MenuCreationStrategy.PHOTO_MENU_CREATION_STRATEGY.getMenuEntries( ( Photo ) menuEntry, accessor, entryMenuOperationType, services );
		}

		if ( menuEntry instanceof User ) {
			return MenuCreationStrategy.USER_MENU_CREATION_STRATEGY.getMenuEntries( ( User ) menuEntry, accessor, entryMenuOperationType, services );
		}

		throw new IllegalArgumentException( String.format( "Illegal popup menu entry type: %s", menuEntry ) );
	}
}
