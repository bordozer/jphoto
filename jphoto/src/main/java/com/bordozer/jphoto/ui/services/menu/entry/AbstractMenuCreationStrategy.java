package com.bordozer.jphoto.ui.services.menu.entry;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuData;
import com.bordozer.jphoto.ui.services.menu.entry.items.PopupMenuAssignable;

import java.util.List;

abstract class AbstractMenuCreationStrategy<T extends PopupMenuAssignable> {

    abstract List<AbstractEntryMenuItem<? extends PopupMenuAssignable>> getMenuEntries(final T menuEntry, final User accessor, final EntryMenuData entryMenuData, final Services services);
}
