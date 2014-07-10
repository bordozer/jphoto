package ui.services.menu.entry;

import core.general.user.User;
import core.services.system.Services;
import ui.services.menu.entry.items.AbstractEntryMenuItem;
import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.PopupMenuAssignable;

import java.util.List;

abstract class AbstractMenuCreationStrategy<T extends PopupMenuAssignable> {

	abstract List<AbstractEntryMenuItem<? extends PopupMenuAssignable>> getMenuEntries( final T menuEntry, final User accessor, final EntryMenuOperationType entryMenuOperationType, final Services services );
}
