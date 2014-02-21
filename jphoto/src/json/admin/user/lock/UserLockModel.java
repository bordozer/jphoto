package json.admin.user.lock;

public class UserLockModel {

	private final int userId;

	public UserLockModel( final int userId ) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}
}
