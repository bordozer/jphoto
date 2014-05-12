package json.icon;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class BookmarkEntryDTO {

	private int userId;
	private int bookmarkEntryId;
	private int bookmarkEntryTypeId;

	private String title;
	private String icon;

	private boolean isAdding;
	private String saveCallbackMessage;

	public BookmarkEntryDTO() {
	}

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

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public int getBookmarkEntryId() {
		return bookmarkEntryId;
	}

	public void setBookmarkEntryId( final int bookmarkEntryId ) {
		this.bookmarkEntryId = bookmarkEntryId;
	}

	public int getBookmarkEntryTypeId() {
		return bookmarkEntryTypeId;
	}

	public void setBookmarkEntryTypeId( final int bookmarkEntryTypeId ) {
		this.bookmarkEntryTypeId = bookmarkEntryTypeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( final String title ) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon( final String icon ) {
		this.icon = icon;
	}

	public boolean isAdding() {
		return isAdding;
	}

	public void setAdding( final boolean isAdding ) {
		this.isAdding = isAdding;
	}

	public String getSaveCallbackMessage() {
		return saveCallbackMessage;
	}

	public void setSaveCallbackMessage( final String saveCallbackMessage ) {
		this.saveCallbackMessage = saveCallbackMessage;
	}
}
