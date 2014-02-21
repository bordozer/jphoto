package admin.controllers.user.lock;

public class UserLockModel {

	private int userId;

	public UserLockModel( final int userId ) {

		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}
}
