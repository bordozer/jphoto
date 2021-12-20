package com.bordozer.jphoto.ui.services.menu.entry.items.photo.goTo;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;

public class PhotoMenuItemGoToAuthorPhotoByGenre extends AbstractPhotoGoToAuthorPhotos {

    public PhotoMenuItemGoToAuthorPhotoByGenre(final Photo photo, final User accessor, final Services services) {
        super(photo, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_GENRE;
    }

    @Override
    public AbstractEntryMenuItemCommand<Photo> getMenuItemCommand() {

        final Genre genre = getGenre();
        final User photoAuthor = getPhotoAuthor();

        return new AbstractEntryMenuItemCommand<Photo>(menuEntry, accessor, services) {

            @Override
            public String getMenuText() {
                return getTranslatorService().translate("PhotoMenuItem: $1: photos in category '$2' ( $3 )", getLanguage(), photoAuthor.getNameEscaped(), getGenreNameTranslated(genre), String.valueOf(getPhotosQty()));
            }

            @Override
            public String getMenuCommand() {
                return String.format("goToMemberPhotosByGenre( %d, %d );", photoAuthor.getId(), genre.getId());
            }
        };
    }

    @Override
    protected int getPhotosQty() {
        return getPhotoService().getPhotosCountByUserAndGenre(menuEntry.getUserId(), menuEntry.getGenreId());
    }

    private Genre getGenre() {
        return getGenreService().load(menuEntry.getGenreId());
    }
}
