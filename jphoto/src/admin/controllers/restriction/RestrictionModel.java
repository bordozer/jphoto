package admin.controllers.restriction;

public class RestrictionModel {

	private int userId;
	private String userName;

	public RestrictionModel( final int userId ) {

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
