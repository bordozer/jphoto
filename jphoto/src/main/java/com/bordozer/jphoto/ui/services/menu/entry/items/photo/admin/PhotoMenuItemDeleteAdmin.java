package com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.commands.PhotoMenuItemDeleteCommand;

public class PhotoMenuItemDeleteAdmin extends AbstractPhotoMenuItemOperationAdmin {

    public PhotoMenuItemDeleteAdmin(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.ADMIN_MENU_ITEM_DELETE;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
        return new PhotoMenuItemDeleteCommand(menuEntry, accessor, services);
    }

    @Override
    protected boolean isSystemConfigurationKeyIsOn() {
        return services.getConfigurationService().getBoolean(ConfigurationKey.ADMIN_CAN_DELETE_OTHER_PHOTOS);
    }

    @Override
    public String getMenuCssClass() {
        return MENU_ITEM_ADMIN_CSS_CLASS;
    }

    @Override
    public String getCallbackMessage() {
        return services.getTranslatorService().translate("PhotoMenuItem: Photo $1 has been deleted", getLanguage(), menuEntry.getNameEscaped());
    }
}
