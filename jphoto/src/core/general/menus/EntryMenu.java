package core.general.menus;

import core.general.photo.PhotoComment;
import utils.TranslatorUtils;

import java.util.List;

public class EntryMenu {

	private final PopupMenuAssignable menuEntry;
	private final EntryMenuType entryMenuType;

	private final List<? extends AbstractEntryMenuItem> entryMenuItems;

	public EntryMenu( final PopupMenuAssignable menuEntry, final EntryMenuType entryMenuType, final List<? extends AbstractEntryMenuItem> entryMenuItems ) {
		this.menuEntry = menuEntry;
		this.entryMenuType = entryMenuType;
		this.entryMenuItems = entryMenuItems;
	}

	public String getMenuTitle() {
		final StringBuilder builder = new StringBuilder();
		builder.append( entryMenuType.getNameTranslated() ).append( ": " ).append( "#" ).append( menuEntry.getId() );

		/*if ( menuEntry instanceof User ) {
			builder.append( ); // TODO: show ADMIN prefix
		}*/

		if ( menuEntry instanceof PhotoComment ) {
			builder.append( ( ( PhotoComment ) menuEntry ).isCommentDeleted() ? TranslatorUtils.translate( " ( deleted )" ) : "" );
		}

		return builder.toString();
	}

	public int getEntryId() {
		return menuEntry.getId();
	}

	public String getMenuId() {
		return String.format( "menu_%d_%d", entryMenuType.getId(), getEntryId() );
	}

	public List<? extends AbstractEntryMenuItem> getEntryMenuItems() {
		return entryMenuItems;
	}

	public EntryMenuType getEntryMenuType() {
		return entryMenuType;
	}

	public int getMenuHeight() {
		int result = 0;

		for ( final AbstractEntryMenuItem entryMenuItem : entryMenuItems ) {
			result += entryMenuItem.getHeight();
		}

		return result;
	}
}
