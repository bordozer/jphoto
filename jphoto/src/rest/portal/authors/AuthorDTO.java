package rest.portal.authors;

public class AuthorDTO {

	private int userId;
	private int userName;
	private int userCardLink;

	public int getUserId() {
		return userId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public int getUserName() {
		return userName;
	}

	public void setUserName( final int userName ) {
		this.userName = userName;
	}

	public int getUserCardLink() {
		return userCardLink;
	}

	public void setUserCardLink( final int userCardLink ) {
		this.userCardLink = userCardLink;
	}
}
