package core.services.menu;

import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.PopupMenuAssignable;
import core.general.user.User;
import core.services.system.Services;

import java.util.List;

abstract class AbstractMenuCreationStrategy<T extends PopupMenuAssignable> {

	abstract List<AbstractEntryMenuItem<? extends PopupMenuAssignable>> getMenuEntries( final T menuEntry, final User accessor, final EntryMenuOperationType entryMenuOperationType, final Services services );
}
