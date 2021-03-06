package com.bordozer.jphoto.ui.services.menu.entry.items.comment.goTo;

import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;

public class CommentMenuItemGoToCommentAuthorPhotos extends AbstractCommentGoToAuthorPhotos {

    public CommentMenuItemGoToCommentAuthorPhotos(final PhotoComment photoComment, final User accessor, final Services services) {
        super(photoComment, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.GO_TO_USER_PHOTOS;
    }

    @Override
    public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {
        final User commentAuthor = menuEntry.getCommentAuthor();
        return new AbstractEntryMenuItemCommand<PhotoComment>(menuEntry, accessor, services) {

            @Override
            public String getMenuText() {
                final int photoQtyByUser = getPhotoService().getPhotosCountByUser(commentAuthor.getId());
                return getTranslatorService().translate("PhotoMenuItem: $1: all photos ( $2 )", getLanguage(), commentAuthor.getNameEscaped(), String.valueOf(photoQtyByUser));
            }

            @Override
            public String getMenuCommand() {
                return String.format("goToMemberPhotos( %d );", commentAuthor.getId());
            }
        };
    }

    @Override
    public int getPhotoQty() {
        return getPhotoService().getPhotosCountByUser(menuEntry.getCommentAuthor().getId());
    }
}
