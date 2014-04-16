package json.admin.user.lock;

public class UserLockModel {

	private final int userId;
	private String userName;
	private String userCardLink;

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

	public String getUserCardLink() {
		return userCardLink;
	}

	public void setUserCardLink( final String userCardLink ) {
		this.userCardLink = userCardLink;
	}
}
