package com.bordozer.jphoto.ui.services.menu.entry.items.comment.operations;

import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.AbstractCommentMenuItem;
import com.bordozer.jphoto.utils.UserUtils;

public class CommentMenuItemDelete extends AbstractCommentMenuItem {

    public CommentMenuItemDelete(final PhotoComment photoComment, final User accessor, final Services services) {
        super(photoComment, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.MENU_ITEM_DELETE;
    }

    @Override
    public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {
        return new AbstractEntryMenuItemCommand<PhotoComment>(menuEntry, accessor, services) {

            @Override
            public String getMenuText() {
                if (isCommentLeftByAccessor()) {
                    return getTranslatorService().translate("CommentMenuItemDelete: Delete comment", getLanguage());
                }

                return getTranslatorService().translate("CommentMenuItemDelete: Delete comment (as photo author)", getLanguage());
            }

            @Override
            public String getMenuCommand() {
                return String.format("deleteComment( %d );", getId());
            }
        };
    }

    @Override
    public boolean isAccessibleFor() {

        if (menuEntry.isCommentDeleted()) {
            return false;
        }

        if (!UserUtils.isLoggedUser(accessor)) {
            return false;
        }

        if (UserUtils.isUserOwnThePhoto(accessor, getPhoto())) {
            return true;
        }

        // TODO: should be allowed deletion of com.bordozer.jphoto.admin messages?

        return isCommentLeftByAccessor();
    }
}
