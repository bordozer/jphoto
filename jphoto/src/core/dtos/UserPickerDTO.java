package core.dtos;

public class UserPickerDTO {

	private String userId;
	private String userName;
	private String userNameEscaped;
	private String userCardLink;
	private String userAvatarUrl;
	private String userGender;

	public String getUserId() {
		return userId;
	}

	public void setUserId( final String userId ) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName( final String userName ) {
		this.userName = userName;
	}

	public String getUserNameEscaped() {
		return userNameEscaped;
	}

	public void setUserNameEscaped( final String userNameEscaped ) {
		this.userNameEscaped = userNameEscaped;
	}

	public String getUserCardLink() {
		return userCardLink;
	}

	public void setUserCardLink( final String userCardLink ) {
		this.userCardLink = userCardLink;
	}

	public String getUserAvatarUrl() {
		return userAvatarUrl;
	}

	public void setUserAvatarUrl( final String userAvatarUrl ) {
		this.userAvatarUrl = userAvatarUrl;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender( final String userGender ) {
		this.userGender = userGender;
	}
}
