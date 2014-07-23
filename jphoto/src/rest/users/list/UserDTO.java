package rest.users.list;

public class UserDTO {

	private int userId;
	private String userName;

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
}
