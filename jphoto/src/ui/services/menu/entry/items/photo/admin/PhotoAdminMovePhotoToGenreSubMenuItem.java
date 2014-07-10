package ui.services.menu.entry.items.photo.admin;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.EntryMenuOperationType;

public class PhotoAdminMovePhotoToGenreSubMenuItem extends AbstractPhotoMenuItemOperationAdmin {

	private final Genre genre;

	public PhotoAdminMovePhotoToGenreSubMenuItem( final Photo photo, final User accessor, final Genre genre, final Services services ) {
		super( photo, accessor, services );

		this.genre = genre;
	}

	@Override
	protected boolean isSystemConfigurationKeyIsOn() {
		return true;
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU_ITEM;
	}

	@Override
	public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand<Photo>( menuEntry, accessor, services ) {
			@Override
			public String getMenuText() {
//				return getTranslatorService().translateGenre( genre, getLanguage() );
				return String.format( "#%d: %s", genre.getId(), genre.getName() );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "alert( 'Move to genre #%d menu item' );", genre.getId() );
			}
		};
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_ADMIN_CSS_CLASS;
	}
}
