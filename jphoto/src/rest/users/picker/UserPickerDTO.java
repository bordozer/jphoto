package rest.users.picker;

import java.util.List;

public class UserPickerDTO {

	private String filter;

	private List<UserDTO> userDTOs;

	public String getFilter() {
		return filter;
	}

	public void setFilter( final String filter ) {
		this.filter = filter;
	}

	public List<UserDTO> getUserDTOs() {
		return userDTOs;
	}

	public void setUserDTOs( final List<UserDTO> userDTOs ) {
		this.userDTOs = userDTOs;
	}
}
