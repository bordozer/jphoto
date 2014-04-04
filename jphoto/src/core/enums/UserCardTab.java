package core.enums;

import utils.StringUtilities;

public enum UserCardTab {

	BRIEF_USER__OVERVIEW( "brief", "UserCardTab: Overview" )
	, PERSONAL_DATA( "personal", "UserCardTab: Personal data" )
	, PHOTOS_OVERVIEW( "photos", "UserCardTab: Photos overview" )
	, STATISTICS( "statistics", "UserCardTab: Statistics" )
	, TEAM( "team", "UserCardTab: Team" )
	, ALBUMS( "albums", "UserCardTab: Albums" )
	, ACTIVITY_STREAM( "activity", "UserCardTab: Activity stream" )
	;

	private final String key;
	private final String name;

	private UserCardTab( final String key, final String name ) {
		this.key = key;
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public static UserCardTab getDefaultUserCardTab() {
		return UserCardTab.BRIEF_USER__OVERVIEW;
	}

	public boolean isDefaultTab() {
		return this == getDefaultUserCardTab();
	}

	public static UserCardTab getById( final String key ) {
		for ( final UserCardTab userCardTab : UserCardTab.values() ) {
			if ( userCardTab.getKey().equals( key ) ) {
				return userCardTab;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal UserCardTab key: %s", key ) );
	}
}
