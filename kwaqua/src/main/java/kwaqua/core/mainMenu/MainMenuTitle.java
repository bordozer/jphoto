package kwaqua.core.mainMenu;

public enum MainMenuTitle {

	PHOTOS( "Photos" )
	, PHOTOS_ALL( PHOTOS, "All photos" )
	, PHOTOS_TODAY( PHOTOS, "Today's photos" )
	, PHOTOS_YESTERDAY( PHOTOS, "Yesterday's photos" )
	, PHOTOS_WEEK( PHOTOS, "This week's photos" )
	, PHOTOS_THIS_MONTH( PHOTOS, "This month's photos" )
	, PHOTOS_AUTHORS( PHOTOS, "Authors" )
	, PHOTOS_MODELS( PHOTOS, "Models" )
	, PHOTOS_MAKEUP_MASTERS( PHOTOS, "Makeup masters" )

	, MEMBERS( "Members" )
	, MEMBERS_ALL( MEMBERS, "All Members" )
	, MEMBERS_AUTHORS( MEMBERS, "Authors" )
	, MEMBERS_MODELS( MEMBERS, "Models" )
	, MEMBERS_MAKEUP_MASTER( MEMBERS, "Makeup masters" )
	;

	private final MainMenuTitle parent;
	private final String title;

	private MainMenuTitle( final String title ) {
		this( null, title );
	}

	private MainMenuTitle( final MainMenuTitle parent, final String title ) {
		this.parent = parent;
		this.title = title;
	}

	public MainMenuTitle getParent() {
		return parent;
	}

	public String getTitle() {
		return title;
	}

	public static MainMenuTitle getByTitle( final String title ) {
		return getByTitle( null, title );
	}

	public static MainMenuTitle getByTitle( final MainMenuTitle parent, final String title ) {
		for ( final MainMenuTitle mainMenuTitle : MainMenuTitle.values() ) {
			if ( mainMenuTitle.getParent() == parent && mainMenuTitle.getTitle().equals( title ) ) {
				return mainMenuTitle;
			}
		}

		throw new IllegalAccessError( String.format( "Unknown MainMenuTitle title: '%s'", title ) );
	}
}
