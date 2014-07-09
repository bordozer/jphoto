package core.general.menus.photo.admin;

import core.general.genre.Genre;
import core.general.menus.*;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoAdminMovePhotoToGenreSubMenu extends AbstractPhotoMenuItem implements SubmenuAccesible  {

	private final List<EntryMenuOperationType> entryMenuOperationTypes;

	public PhotoAdminMovePhotoToGenreSubMenu( final Photo photo, final User accessor, final Services services ) {
		super( photo, accessor, services );

		entryMenuOperationTypes = newArrayList();

		final EntryMenuOperationType menuItem = EntryMenuOperationType.ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU_ITEM;

		// TODO: fake test data
		final Genre genre = new Genre();
		genre.setId( 666 );
		genre.setName( "Fake genre" );
		menuItem.setCustomObject( genre );

		entryMenuOperationTypes.add( menuItem );

		// TODO: real scenario
		/*final List<Genre> genres = services.getGenreService().loadAllSortedByNameForLanguage( getLanguage() );
		for ( final Genre genre : genres ) {

			if ( menuEntry.getGenreId() == genre.getId() ) {
				continue;
			}

			final EntryMenuOperationType menuItem = EntryMenuOperationType.ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU_ITEM;
			menuItem.setCustomObject( genre );
			entryMenuOperationTypes.add( menuItem );
		}*/
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, accessor, services ) {
			@Override
			public String getMenuText() {
				return getTranslatorService().translate( ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU_ENTRY_TEXT, getLanguage() );
			}

			@Override
			public String getMenuCommand() {
				return ADMIN_SUB_MENU_ENTRY_COMMAND;
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return isAccessorSuperAdmin();
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_CSS_CLASS_ADMIN;
	}

	@Override
	public EntryMenu getEntrySubMenu() {
		return new EntryMenu( menuEntry, EntryMenuType.PHOTO, getSubMenus(), getLanguage(), services );
	}

	private List<? extends AbstractEntryMenuItem> getSubMenus() {
		return services.getEntryMenuService().getPhotoMenu( menuEntry, accessor, entryMenuOperationTypes ).getEntryMenuItems();
	}
}
