package core.services.translator.nerds;

public enum LinkNerdText {

	USER_STATISTICS_THE_USER_IS_ADDED_IN_FAVORITE_MEMBERS_BY(
		"User Statistics: The user is added in favorite members by"
		, "User Statistics: The list of users who added $1 to the favorite members" )
	, USER_STATISTICS_COMMENTS_RECEIVED(
		"User Statistics: User's received comments"
		, "User Statistics: Comments to $1's photos" )
	, USER_STATISTICS_COMMENTS_RECEIVED_UNREAD(
		"User Statistics: User's received unread comments"
		, "User Statistics: Unread by $1 comments" )
	, USER_STATISTICS_COMMENTS_WRITTEN(
		"User Statistics: Written by user comments"
		, "User Statistics: Written by $1 comments" )
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
