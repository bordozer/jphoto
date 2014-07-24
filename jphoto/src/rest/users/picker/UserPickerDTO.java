package rest.users.picker;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserPickerDTO {

	private String searchString;
	private UserDTO selectedUserDTO;

	private List<UserDTO> userDTOs;

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString( final String searchString ) {
		this.searchString = searchString;
	}

	public UserDTO getSelectedUserDTO() {
		return selectedUserDTO;
	}

	public void setSelectedUserDTO( final UserDTO selectedUserDTO ) {
		this.selectedUserDTO = selectedUserDTO;
	}

	public List<UserDTO> getUserDTOs() {
		return userDTOs;
	}

	public void setUserDTOs( final List<UserDTO> userDTOs ) {
		this.userDTOs = userDTOs;
	}
}
