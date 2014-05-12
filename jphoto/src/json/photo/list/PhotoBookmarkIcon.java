package json.photo.list;

public class PhotoBookmarkIcon {

	private int favoriteEntryTypeId;

	public PhotoBookmarkIcon() {
	}

	public PhotoBookmarkIcon( final int favoriteEntryTypeId ) {
		this.favoriteEntryTypeId = favoriteEntryTypeId;
	}

	public int getFavoriteEntryTypeId() {
		return favoriteEntryTypeId;
	}

	public void setFavoriteEntryTypeId( final int favoriteEntryTypeId ) {
		this.favoriteEntryTypeId = favoriteEntryTypeId;
	}

	@Override
	public String toString() {
		return String.format( "%d", favoriteEntryTypeId );
	}
}
