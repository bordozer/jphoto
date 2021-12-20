package com.bordozer.jphoto.ui.services.menu.entry.items.photo.photo;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

public class PhotoMenuItemInfo extends AbstractPhotoMenuItem {

    public PhotoMenuItemInfo(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.PHOTO_INFO;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
        return new AbstractEntryMenuItemCommand<Photo>(menuEntry, accessor, services) {
            @Override
            public String getMenuText() {
                return getTranslatorService().translate("PhotoMenuItem: Photo info", getLanguage());
            }

            @Override
            public String getMenuCommand() {
                final int photoId = getId();
                final String infoLink = services.getUrlUtilsService().getPhotoInfoLink(photoId);

                return String.format("openPopupWindowCustom( '%s', 'photoInfo_%d', 460, 800, 100, 100 );", infoLink, photoId);
            }
        };
    }

    @Override
    public boolean isAccessibleFor() {
        return true;
    }
}
