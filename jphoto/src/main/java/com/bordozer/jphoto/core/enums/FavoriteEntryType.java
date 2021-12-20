package com.bordozer.jphoto.core.enums;

import com.bordozer.jphoto.core.interfaces.IdentifiableNameable;

import java.util.EnumSet;

public enum FavoriteEntryType implements IdentifiableNameable {

    FAVORITE_MEMBERS(1
            , "FavoriteEntryType: Favorite members"
            , "FavoriteEntryType: Favorite members - add"
            , "FavoriteEntryType: Favorite members - remove"
            , "favorite-members-add.png"
            , "favorite-members-remove.png"
    ), FAVORITE_PHOTOS(2
            , "FavoriteEntryType: Favorite photos"
            , "FavoriteEntryType: Favorite photos - add"
            , "FavoriteEntryType: Favorite photos - remove"
            , "favorite-photos-add.png"
            , "favorite-photos-remove.png"
    ), FRIENDS(3
            , "FavoriteEntryType: Friends"
            , "FavoriteEntryType: Friends - add"
            , "FavoriteEntryType: Friends - remove"
            , "friends-add.png"
            , "friends-remove.png"
    ), BLACKLIST(4
            , "FavoriteEntryType: Black list"
            , "FavoriteEntryType: Black list - add"
            , "FavoriteEntryType: Black list - remove"
            , "black-list-add.png"
            , "black-list-remove.png"
    ), BOOKMARKED_PHOTOS(5
            , "FavoriteEntryType: Bookmarked photo"
            , "FavoriteEntryType: Bookmarked photo - add"
            , "FavoriteEntryType: Bookmarked photo - remove"
            , "bookmarked-photo-add.png"
            , "bookmarked-photo-remove.png"
    ), NEW_PHOTO_NOTIFICATION(6
            , "FavoriteEntryType: New photos tracking"
            , "FavoriteEntryType: New photos tracking - add"
            , "FavoriteEntryType: New photos tracking - remove"
            , "new-photos-tracking-add.png"
            , "new-photos-tracking-remove.png"
    ), NEW_COMMENTS_NOTIFICATION(7
            , "FavoriteEntryType: Photo comments tracking"
            , "FavoriteEntryType: Photo comments tracking - add"
            , "FavoriteEntryType: Photo comments tracking - remove"
            , "photo-comments-tracking-add.png"
            , "photo-comments-tracking-remove.png"
    ), MEMBERS_INVISIBILITY_LIST(8
            , "FavoriteEntryType: Members invisibility list"
            , "FavoriteEntryType: Members invisibility list - add"
            , "FavoriteEntryType: Members invisibility list - remove"
            , "members-invisibility-list-add.png"
            , "members-invisibility-list-remove.png"
    );

    public static final String FAVORITES_IMAGE_FOLDER = "favorites";

    public final static EnumSet<FavoriteEntryType> RELATED_TO_PHOTO = EnumSet.<FavoriteEntryType>of(FAVORITE_PHOTOS, BOOKMARKED_PHOTOS, NEW_COMMENTS_NOTIFICATION);

    private final int id;

    private final String name;
    private final String addText;
    private final String removeText;

    private final String addIcon;
    private final String removeIcon;

    private FavoriteEntryType(final int id, final String name, final String addText, final String removeText, final String addIcon, final String removeIcon) {
        this.id = id;

        this.name = name;
        this.addText = addText;
        this.removeText = removeText;

        this.addIcon = addIcon;
        this.removeIcon = removeIcon;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddText() {
        return addText;
    }

    public String getRemoveText() {
        return removeText;
    }

    public String getAddIcon() {
        return addIcon;
    }

    public String getRemoveIcon() {
        return removeIcon;
    }

    public boolean isRelatedToPhoto() {
        return RELATED_TO_PHOTO.contains(this);
    }

    private String getIcon(final String icon) {
        //		return String.format( "%s/%s", FAVORITES_IMAGE_FOLDER, icon );
        return icon;
    }

    public static FavoriteEntryType getById(final int id) {
        for (FavoriteEntryType entryType : FavoriteEntryType.values()) {
            if (entryType.getId() == id) {
                return entryType;
            }
        }

        return null;
    }
}
