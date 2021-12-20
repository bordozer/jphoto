package com.bordozer.jphoto.ui.services.menu.entry.items.photo.operation;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.commands.PhotoMenuItemDeleteCommand;

public class PhotoMenuItemDelete extends AbstractPhotoUserOperationsMenuItem {

    public PhotoMenuItemDelete(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.MENU_ITEM_DELETE;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
        return new PhotoMenuItemDeleteCommand(menuEntry, accessor, services);
    }

    @Override
    public boolean hasAccessTo() {
        return getSecurityService().userCanDeletePhoto(accessor, menuEntry);
    }
}
