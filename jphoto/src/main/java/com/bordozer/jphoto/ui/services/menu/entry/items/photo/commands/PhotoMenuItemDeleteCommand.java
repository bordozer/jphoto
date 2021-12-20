package com.bordozer.jphoto.ui.services.menu.entry.items.photo.commands;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;

public class PhotoMenuItemDeleteCommand extends AbstractEntryMenuItemCommand<Photo> {

    public PhotoMenuItemDeleteCommand(final Photo menuEntry, final User accessor, Services services) {
        super(menuEntry, accessor, services);
    }

    @Override
    public String getMenuText() {
        return getTranslatorService().translate("PhotoMenuItem: Delete photo", getLanguage());
    }

    @Override
    public String getMenuCommand() {
        return String.format("deletePhotoFromContextMenu();");
    }
}
