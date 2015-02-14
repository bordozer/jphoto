package core.enums;

import core.interfaces.IdentifiableNameable;

import java.util.EnumSet;

public enum FavoriteEntryType implements IdentifiableNameable {

	FAVORITE_MEMBERS( 1
		, "FavoriteEntryType: Favorite members"
		, "FavoriteEntryType: Favorite members - add"
		, "FavoriteEntryType: Favorite members - remove"
		, "userAdd16x16.png"
		, "userRemove16x16.png"
	)
	, FAVORITE_PHOTOS( 2
		, "FavoriteEntryType: Favorite photos"
		, "FavoriteEntryType: Favorite photos - add"
		, "FavoriteEntryType: Favorite photos - remove"
		, "photoAdd16x16.png"
		, "photoRemove16x16.png"
	)
	, FRIENDS( 3
		, "FavoriteEntryType: Friends"
		, "FavoriteEntryType: Friends - add"
		, "FavoriteEntryType: Friends - remove"
		, "friendAdd16x16.png"
		, "friendRemove16x16.png"
	)
	, BLACKLIST( 4
		, "FavoriteEntryType: Black list"
		, "FavoriteEntryType: Black list - add"
		, "FavoriteEntryType: Black list - remove"
		, "blackListAdd16x16.png"
		, "blackListRemove16x16.png"
	)
	, BOOKMARKED_PHOTOS( 5
		, "FavoriteEntryType: Bookmarked photo"
		, "FavoriteEntryType: Bookmarked photo - add"
		, "FavoriteEntryType: Bookmarked photo - remove"
		, "bookmarkAdd16x16.png"
		, "bookmarkRemove16x16.png"
	)
	, NEW_PHOTO_NOTIFICATION( 6
		, "FavoriteEntryType: New photos notification"
		, "FavoriteEntryType: New photos notification - add"
		, "FavoriteEntryType: New photos notification - remove"
		, "newPhotoNotificationAdd16x16.png"
		, "newPhotoNotificationRemove16x16.png"
	)
	, NEW_COMMENTS_NOTIFICATION( 7
		, "FavoriteEntryType: New comments to photo notification"
		, "FavoriteEntryType: New comments to photo notification - add"
		, "FavoriteEntryType: New comments to photo notification - remove"
		, "newCommentsNotificationAdd16x16.png"
		, "newCommentsNotificationRemove16x16.png"
	)
	, MEMBERS_INVISIBILITY_LIST( 8
		, "FavoriteEntryType: Authors invisibility list"
		, "FavoriteEntryType: Authors invisibility list - add"
		, "FavoriteEntryType: Authors invisibility list - remove"
		, "members-invisibility-list-add.png"
		, "members-invisibility-list-remove.png"
	)
	;

	public static final String FAVORITES_IMAGE_FOLDER = "favorites";

	public final static EnumSet<FavoriteEntryType> RELATED_TO_PHOTO = EnumSet.<FavoriteEntryType>of( FAVORITE_PHOTOS, BOOKMARKED_PHOTOS, NEW_COMMENTS_NOTIFICATION );

	private final int id;

	private final String name;
	private final String addText;
	private final String removeText;

	private final String addIcon;
	private final String removeIcon;

	private FavoriteEntryType( final int id, final String name, final String addText, final String removeText, final String addIcon, final String removeIcon ) {
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
		return RELATED_TO_PHOTO.contains( this );
	}

	private String getIcon( final String icon) {
//		return String.format( "%s/%s", FAVORITES_IMAGE_FOLDER, icon );
		return icon;
	}

	public static FavoriteEntryType getById( final int id ) {
		for ( FavoriteEntryType entryType : FavoriteEntryType.values() ) {
			if ( entryType.getId() == id ) {
				return entryType;
			}
		}

		return null;
	}
}
