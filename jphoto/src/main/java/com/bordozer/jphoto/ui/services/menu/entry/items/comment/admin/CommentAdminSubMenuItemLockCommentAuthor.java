package com.bordozer.jphoto.ui.services.menu.entry.items.comment.admin;

import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.AbstractCommentMenuItem;

public class CommentAdminSubMenuItemLockCommentAuthor extends AbstractCommentMenuItem {

    public CommentAdminSubMenuItemLockCommentAuthor(final PhotoComment photoComment, final User accessor, final Services services) {
        super(photoComment, accessor, services);
    }

    @Override
    public EntryMenuOperationType getEntryMenuType() {
        return EntryMenuOperationType.ADMIN_SUB_MENU_LOCK_USER;
    }

    @Override
    public AbstractEntryMenuItemCommand<PhotoComment> getMenuItemCommand() {

        final User commentAuthor = menuEntry.getCommentAuthor();

        return new AbstractEntryMenuItemCommand<PhotoComment>(menuEntry, accessor, services) {
            @Override
            public String getMenuText() {
                return getTranslatorService().translate("Restrict comment author: $1", getLanguage(), commentAuthor.getNameEscaped());
            }

            @Override
            public String getMenuCommand() {
                return String.format("adminRestrictUser( %d, '%s' );", commentAuthor.getId(), commentAuthor.getNameEscaped());
            }
        };
    }

    @Override
    public boolean isAccessibleFor() {

        if (isCommentAuthorSuperAdmin()) {
            return false;
        }

        if (!isAccessorSuperAdmin()) {
            return false;
        }

        return !isCommentLeftByAccessor();
    }

    @Override
    public String getMenuCssClass() {
        return MENU_ITEM_ADMIN_CSS_CLASS;
    }
}
