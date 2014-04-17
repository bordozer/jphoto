package admin.controllers.user.lock;

public class UserLockModel {

	private int userId;
	private String userName;

	public UserLockModel( final int userId ) {

		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName( final String userName ) {
		this.userName = userName;
	}
}
