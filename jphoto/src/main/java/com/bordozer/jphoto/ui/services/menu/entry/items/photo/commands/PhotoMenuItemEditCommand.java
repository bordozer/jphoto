package com.bordozer.jphoto.ui.services.menu.entry.items.photo.commands;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;

public class PhotoMenuItemEditCommand extends AbstractEntryMenuItemCommand<Photo> {

    public PhotoMenuItemEditCommand(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    public String getMenuText() {
        return getTranslatorService().translate("PhotoMenuItem: Edit photo", getLanguage());
    }

    @Override
    public String getMenuCommand() {
        return String.format("editPhotoData( %d );", getId());
    }
}
