package com.bordozer.jphoto.ui.services.menu.entry.items.photo.user;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.AbstractPhotoMenuItem;

public class PhotoMenuItemSendPrivateMessage extends AbstractPhotoMenuItem {

    public PhotoMenuItemSendPrivateMessage(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.SEND_PRIVATE_MESSAGE;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {
        return new AbstractEntryMenuItemCommand<Photo>(menuEntry, accessor, services) {

            private User photoAuthor = getPhotoAuthor();

            @Override
            public String getMenuText() {
                return getTranslatorService().translate("PhotoMenuItem: Send private message to $1", getLanguage(), photoAuthor.getNameEscaped());
            }

            @Override
            public String getMenuCommand() {
                return String.format("sendPrivateMessage( %d, %d, '%s' );", accessor.getId(), photoAuthor.getId(), photoAuthor.getNameEscaped());
            }
        };
    }

    @Override
    public boolean isAccessibleFor() {

        if (!isMenuAccessorLogged()) {
            return false;
        }

        if (isAccessorSeeingMenuOfOwnPhoto()) {
            return false;
        }

        if (isSuperAdminUser(accessor)) {
            return true;
        }

        if (isPhotoIsWithinAnonymousPeriod()) {
            return false;
        }

        return !getFavoritesService().isUserInBlackListOfUser(menuEntry.getUserId(), accessor.getId());

    }
}
