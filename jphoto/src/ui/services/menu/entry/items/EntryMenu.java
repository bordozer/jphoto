package ui.services.menu.entry.items;

import core.general.photo.PhotoComment;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;

import java.util.List;

public class EntryMenu {

	private final PopupMenuAssignable menuEntry;
	private final EntryMenuType entryMenuType;

	private final List<? extends AbstractEntryMenuItem> entryMenuItems;
	private Language language;
	private Services services;

	public EntryMenu( final PopupMenuAssignable menuEntry, final EntryMenuType entryMenuType, final List<? extends AbstractEntryMenuItem> entryMenuItems, final Language language, final Services services ) {
		this.menuEntry = menuEntry;
		this.entryMenuType = entryMenuType;
		this.entryMenuItems = entryMenuItems;
		this.language = language;
		this.services = services;
	}

	public String getMenuTitle() {

		final TranslatorService translatorService = services.getTranslatorService();

		final StringBuilder builder = new StringBuilder();
		builder.append( translatorService.translate( entryMenuType.getName(), language ) ).append( ": " ).append( "#" ).append( menuEntry.getId() );

		/*if ( menuEntry instanceof User ) {
			builder.append( ); // TODO: show ADMIN prefix
		}*/

		if ( menuEntry instanceof PhotoComment && ( ( PhotoComment ) menuEntry ).isCommentDeleted() ) {
			builder.append( " " ).append(  translatorService.translate( "( MenuTitle: the comment is deleted )", language ) );
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
