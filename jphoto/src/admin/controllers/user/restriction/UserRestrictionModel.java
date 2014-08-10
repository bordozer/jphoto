package admin.controllers.user.restriction;

public class UserRestrictionModel {

	private int userId;
	private String userName;

	public UserRestrictionModel( final int userId ) {

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
