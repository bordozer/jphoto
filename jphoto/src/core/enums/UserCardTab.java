package core.enums;

import utils.StringUtilities;
import utils.TranslatorUtils;

public enum UserCardTab {

	BRIEF_USER__OVERVIEW( "brief", "Overview" )
	, PERSONAL_DATA( "personal", "Personal data" )
	, PHOTOS_OVERVIEW( "photos", "Photos overview" )
	, STATISTICS( "statistics", "Statistics" )
	, TEAM( "team", "Team" )
	, ALBUMS( "albums", "Albums" )
	, ACTIVITY_STREAM( "activity", "Activity stream" )
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

	public String getNameTranslated() {
		return StringUtilities.toUpperCaseFirst( TranslatorUtils.translate( name ) );
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
