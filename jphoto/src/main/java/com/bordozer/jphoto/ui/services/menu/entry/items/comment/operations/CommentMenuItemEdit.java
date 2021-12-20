package com.bordozer.jphoto.ui.services.menu.entry.items.comment.operations;

import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.AbstractCommentMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.commands.CommentMenuItemEditCommand;
import com.bordozer.jphoto.utils.UserUtils;

public class CommentMenuItemEdit extends AbstractCommentMenuItem {

    public CommentMenuItemEdit(final PhotoComment photoComment, final User accessor, final Services services) {
        super(photoComment, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.MENU_ITEM_EDIT;
    }

    @Override
    public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {
        return new CommentMenuItemEditCommand(menuEntry, accessor, services);
    }

    @Override
    public boolean isAccessibleFor() {

        if (menuEntry.isCommentDeleted()) {
            return false;
        }

        if (!UserUtils.isLoggedUser(accessor)) {
            return false;
        }

        return isCommentLeftByAccessor();
    }
}
