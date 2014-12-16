package rest.editableList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public abstract class EditableListEntryDTO {

	protected int entryId;
	protected int userId;

	public EditableListEntryDTO() {
	}

	public EditableListEntryDTO( int entryId ) {
		this.entryId = entryId;
	}

	public int getEntryId() {
		return entryId;
	}

	public void setEntryId( int entryId ) {
		this.entryId = entryId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}
}
