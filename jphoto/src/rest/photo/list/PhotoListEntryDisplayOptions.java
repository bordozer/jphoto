package rest.photo.list;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class PhotoListEntryDisplayOptions {

	private boolean groupOperationEnabled;

	public boolean isGroupOperationEnabled() {
		return groupOperationEnabled;
	}

	public void setGroupOperationEnabled( final boolean groupOperationEnabled ) {
		this.groupOperationEnabled = groupOperationEnabled;
	}
}
