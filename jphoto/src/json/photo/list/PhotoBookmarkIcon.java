package json.photo.list;

public class PhotoBookmarkIcon {

	private int favoriteEntryTypeId;
	private boolean isAdding;

	public PhotoBookmarkIcon() {
	}

	public PhotoBookmarkIcon( final int favoriteEntryTypeId, final boolean isAdding ) {
		this.favoriteEntryTypeId = favoriteEntryTypeId;
		this.isAdding = isAdding;
	}

	public int getFavoriteEntryTypeId() {
		return favoriteEntryTypeId;
	}

	public void setFavoriteEntryTypeId( final int favoriteEntryTypeId ) {
		this.favoriteEntryTypeId = favoriteEntryTypeId;
	}

	public boolean isAdding() {
		return isAdding;
	}

	public void setAdding( final boolean isAdding ) {
		this.isAdding = isAdding;
	}
}
