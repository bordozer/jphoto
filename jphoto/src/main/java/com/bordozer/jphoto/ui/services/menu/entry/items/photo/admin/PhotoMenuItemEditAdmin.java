package com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.commands.PhotoMenuItemEditCommand;

public class PhotoMenuItemEditAdmin extends AbstractPhotoMenuItemOperationAdmin {

    public PhotoMenuItemEditAdmin(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.ADMIN_MENU_ITEM_EDIT;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
        return new PhotoMenuItemEditCommand(menuEntry, accessor, services);
    }

    @Override
    protected boolean isSystemConfigurationKeyIsOn() {
        return services.getConfigurationService().getBoolean(ConfigurationKey.ADMIN_CAN_EDIT_OTHER_PHOTOS);
    }

    @Override
    public String getMenuCssClass() {
        return MENU_ITEM_ADMIN_CSS_CLASS;
    }
}
