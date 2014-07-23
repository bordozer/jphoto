package rest.users.picker;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserPickerDTO {

	private String searchString;
	private String callback;
	private boolean found;

	private List<UserDTO> userDTOs;

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString( final String searchString ) {
		this.searchString = searchString;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback( final String callback ) {
		this.callback = callback;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound( final boolean found ) {
		this.found = found;
	}

	public List<UserDTO> getUserDTOs() {
		return userDTOs;
	}

	public void setUserDTOs( final List<UserDTO> userDTOs ) {
		this.userDTOs = userDTOs;
	}
}
