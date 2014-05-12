package core.enums;

import core.interfaces.IdentifiableNameable;

import java.util.EnumSet;

public enum FavoriteEntryType implements IdentifiableNameable {

	FAVORITE_MEMBERS( 1, "FavoriteEntryType: Favorite members", "add to favorite members", "The member is in your favorites. Click to remove.", "userAdd16x16.png", "userRemove16x16.png" )
	, FAVORITE_PHOTOS( 2, "FavoriteEntryType: Favorite photos", "add to favorite photos", "The photo is in your favorites. Click to remove.", "photoAdd16x16.png", "photoRemove16x16.png" )
	, FRIENDS( 3, "FavoriteEntryType: Friends", "add to friends", "The member is your friend. Click to remove.", "friendAdd16x16.png", "friendRemove16x16.png" )
	, BLACKLIST( 4, "FavoriteEntryType: Black list", "add to black list", "The member is in your black list. Click to remove.", "blackListAdd16x16.png", "blackListRemove16x16.png" )
	, BOOKMARKED_PHOTOS( 5, "FavoriteEntryType: Bookmarks", "add photo to bookmarks", "The photo is in your bookmarks. Click to remove.", "bookmarkAdd16x16.png", "bookmarkRemove16x16.png" )
	, NEW_PHOTO_NOTIFICATION( 6, "FavoriteEntryType: New photos notification", "add notification about new photos", "You get notification about new photos. Click to unsubscribe", "newPhotoNotificationAdd16x16.png", "newPhotoNotificationRemove16x16.png" )
	, NEW_COMMENTS_NOTIFICATION( 7, "FavoriteEntryType: New comments notification", "You did not subscribe notification about new comments", "You get notification about new comments. Click to unsubscribe", "newCommentsNotificationAdd16x16.png", "newCommentsNotificationRemove16x16.png" )
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
		return String.format( "%s/%s", FAVORITES_IMAGE_FOLDER, icon );
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
