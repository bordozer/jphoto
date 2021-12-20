package com.bordozer.jphoto.ui.services.menu.entry.items.comment.commands;

import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;

public class CommentMenuItemEditCommand extends AbstractEntryMenuItemCommand<PhotoComment> {

    public CommentMenuItemEditCommand(final PhotoComment menuEntry, final User accessor, final Services services) {
        super(menuEntry, accessor, services);
    }

    @Override
    public String getMenuText() {
        return getTranslatorService().translate("CommentMenuItemEdit: Edit comment", getLanguage());
    }

    @Override
    public String getMenuCommand() {
        return String.format("editComment( %d );", getId());
    }
}
