package rest.portal.authors;

public class AuthorDTO {

	private int userId;
	private String userName;
	private String userCardLink;

	private int rating;

	public int getUserId() {
		return userId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
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

	public int getRating() {
		return rating;
	}

	public void setRating( final int rating ) {
		this.rating = rating;
	}
}
