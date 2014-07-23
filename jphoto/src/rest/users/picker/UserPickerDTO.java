package rest.users.picker;

import java.util.List;

public class UserPickerDTO {

	private String searchString;
	private String callback;

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

	public List<UserDTO> getUserDTOs() {
		return userDTOs;
	}

	public void setUserDTOs( final List<UserDTO> userDTOs ) {
		this.userDTOs = userDTOs;
	}
}
