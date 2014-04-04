package core.services.translator;

public enum LinkNerdText {

	USER_STATISTICS_THE_USER_IS_ADDED_IN_FAVORITE_MEMBERS_BY(
		"User Statistics: The user is added in favorite members by"
		, "The list of users who added $1 to the favorite members" )
	;


	private final String text;
	private final String title;

	private LinkNerdText( final String text, final String title ) {
		this.text = text;
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}
}
