package rest.photo.list;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class PhotoListEntryDisplayOptions {

	private boolean isGroupOperationEnabled;

	public boolean isGroupOperationEnabled() {
		return isGroupOperationEnabled;
	}

	public void setGroupOperationEnabled( final boolean isGroupOperationEnabled ) {
		this.isGroupOperationEnabled = isGroupOperationEnabled;
	}
}
