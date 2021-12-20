package com.bordozer.jphoto.ui.services.menu.entry.items;

import java.util.EnumSet;

public enum EntryMenuOperationType {

    SEPARATOR(0, ""), MENU_ITEM_EDIT(1, "menu_edit.png"), MENU_ITEM_DELETE(2, "menu_delete.png"), GO_TO_USER_PHOTOS(11, "menu_go_to_photos.png"), GO_TO_USER_PHOTOS_BY_GENRE(12, "menu_go_to_photos.png"), GO_TO_USER_PHOTOS_BY_TEAM_MEMBER(13, "menu_go_to_photos.png"), GO_TO_USER_PHOTOS_BY_ALBUM(14, "menu_go_to_photos.png"), COMMENT_COMPLAINT_SPAM(21, "menu_comment_spam.png"), COMMENT_COMPLAINT_SWORD_WORDS(22, "menu_comment_sword_words.png"), COMMENT_COMPLAINT_CUSTOM(23, "menu_comment_custom_complaint.png"), COMMENT_REPLY(31, "menu_comment_reply.png"), BLACK_LIST_ADD(41, "menu_black_list_add.png"), BLACK_LIST_REMOVE(42, "menu_black_list_remove.png"), SEND_PRIVATE_MESSAGE(51, "menu_send_private_message.png"), PHOTO_INFO(61, "menu_photo_info.png"), PHOTO_COMPLAINT_COPYRIGHT(62, "menu_photo_complaint_copyright.png"), ADMIN_SUB_MENU(71, "menu_sub_menu.png"), ADMIN_SUB_MENU_LOCK_USER(81, "lock_user.png"), ADMIN_SUB_MENU_LOCK_PHOTO(80, "lock_user.png"), ADMIN_MENU_ITEM_EDIT(82, "menu_edit.png"), ADMIN_MENU_ITEM_DELETE(83, "menu_delete.png"), ADMIN_MENU_ITEM_NUDE_CONTENT_SET(84, "menu_photo_nude_content_is_not_set.png"), ADMIN_MENU_ITEM_NUDE_CONTENT_REMOVE(85, "menu_photo_nude_content_set.png"), ADMIN_MENU_ITEM_GENERATE_PREVIEW(86, "generate_preview.png"), ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU(91, "menu_sub_menu.png"), ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU_ITEM(92, "menu_sub_menu.png") // TODO: icon

    ;

    private static EnumSet<EntryMenuOperationType> SUB_MENUS = EnumSet.of(ADMIN_SUB_MENU, ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU);

    private final int id;
    private final String icon;

    private Object customObject;

    private EntryMenuOperationType(final int id, final String icon) {
        this.id = id;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }

    public static EntryMenuOperationType getById(final int id) {
        for (final EntryMenuOperationType entryMenuOperationType : EntryMenuOperationType.values()) {
            if (entryMenuOperationType.getId() == id) {
                return entryMenuOperationType;
            }
        }

        throw new IllegalArgumentException(String.format("Illegal EntryMenuOperationType id: %d", id));
    }

    public boolean isSubMenu() {
        return SUB_MENUS.contains(this);
    }

    public Object getCustomObject() {
        return customObject;
    }

    public void setCustomObject(final Object customObject) {
        this.customObject = customObject;
    }
}
