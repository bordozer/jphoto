package rest.users.picker;

public class UserDTO {

	private int userId;
	private String userName;
	private String userMembershipTypeName;

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserName( final String userName ) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public String toString() {
		return String.format( "%d: %s", userId, userName );
	}

	public void setUserMembershipTypeName( final String userMembershipTypeName ) {
		this.userMembershipTypeName = userMembershipTypeName;
	}

	public String getUserMembershipTypeName() {
		return userMembershipTypeName;
	}
}
