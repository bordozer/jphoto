package kwaqua.core.mainMenu;

public enum MainMenuTitle {

	PHOTOS( "Photo gallery root" )
	, PHOTOS_ALL( "Photo gallery root" )
	, PHOTOS_TODAY( "Main menu: Today's photos" )
	, PHOTOS_YESTERDAY( "Yesterday's photos" )
	, PHOTOS_WEEK( "This week's photos" )
	, PHOTOS_THIS_MONTH( "This month's photos" )
	, PHOTOS_AUTHORS( "Authors" )
	, PHOTOS_MODELS( "Models" )
	, PHOTOS_MAKEUP_MASTERS( "Makeup masters" )

	, MEMBERS( "Main menu: Members" )
	, MEMBERS_ALL( "Main menu: All members" )
	, MEMBERS_AUTHORS( "Main menu: members: UserMembershipType: author" )
	, MEMBERS_MODELS( "Main menu: members: UserMembershipType: model" )
	, MEMBERS_MAKEUP_MASTER( "Main menu: members: UserMembershipType: makeup master" )
	, MEMBERS_ACTIVITY_STREAM( "Main menu: Activity stream" )
	;

	private final String title;

	private MainMenuTitle( final String title ) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public static MainMenuTitle getByTitle( final String title ) {
		for ( final MainMenuTitle mainMenuTitle : MainMenuTitle.values() ) {
			if ( mainMenuTitle.getTitle().equals( title ) ) {
				return mainMenuTitle;
			}
		}

		throw new IllegalAccessError( String.format( "Unknown MainMenuTitle title: '%s'", title ) );
	}
}
