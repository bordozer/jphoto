package json.icon;

public class BookmarkEntryDTO {

	private final int userId;
	private final int bookmarkEntryId;
	private final int bookmarkEntryTypeId;

	private final String title;
	private final String icon;

	public BookmarkEntryDTO( final int userId, final int bookmarkEntryId, final int bookmarkEntryTypeId, final String title, final String icon ) {
		this.userId = userId;
		this.bookmarkEntryId = bookmarkEntryId;
		this.bookmarkEntryTypeId = bookmarkEntryTypeId;
		this.title = title;
		this.icon = icon;
	}

	public int getUserId() {
		return userId;
	}

	public int getBookmarkEntryId() {
		return bookmarkEntryId;
	}

	public int getBookmarkEntryTypeId() {
		return bookmarkEntryTypeId;
	}

	public String getTitle() {
		return title;
	}

	public String getIcon() {
		return icon;
	}
}
