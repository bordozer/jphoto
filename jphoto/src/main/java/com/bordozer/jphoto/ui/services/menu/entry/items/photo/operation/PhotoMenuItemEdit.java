package com.bordozer.jphoto.ui.services.menu.entry.items.photo.operation;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.commands.PhotoMenuItemEditCommand;

public class PhotoMenuItemEdit extends AbstractPhotoUserOperationsMenuItem {

    public PhotoMenuItemEdit(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.MENU_ITEM_EDIT;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
        return new PhotoMenuItemEditCommand(menuEntry, accessor, services);
    }

    @Override
    public boolean hasAccessTo() {
        return getSecurityService().userCanEditPhoto(accessor, menuEntry);
    }

}
