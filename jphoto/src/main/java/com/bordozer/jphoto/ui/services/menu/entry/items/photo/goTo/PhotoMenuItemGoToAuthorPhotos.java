package com.bordozer.jphoto.ui.services.menu.entry.items.photo.goTo;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;

public class PhotoMenuItemGoToAuthorPhotos extends AbstractPhotoGoToAuthorPhotos {

    public PhotoMenuItemGoToAuthorPhotos(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.GO_TO_USER_PHOTOS;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {

        final User photoAuthor = getPhotoAuthor();

        return new AbstractEntryMenuItemCommand<Photo>(menuEntry, accessor, services) {

            @Override
            public String getMenuText() {
                return getTranslatorService().translate("PhotoMenuItem: $1: all photos ( $2 )", getLanguage(), photoAuthor.getNameEscaped(), String.valueOf(getPhotosQty()));
            }

            @Override
            public String getMenuCommand() {
                return String.format("goToMemberPhotos( %d );", photoAuthor.getId());
            }
        };
    }

    @Override
    protected int getPhotosQty() {
        return getPhotoService().getPhotosCountByUser(menuEntry.getUserId());
    }
}
