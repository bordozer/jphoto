package core.general.configuration;

public enum ConfigurationTab {

	ALL( "all", "All tabs" )
	, CHANGES_ONLY( "changes", "Changes only" )
	, SYSTEM( "system", "System" )
	, CANDIDATES( "candidates", "Candidates" )
	, MEMBERS( "users", "Members" )
	, AVATAR( "avatar", "Avatar" )
	, PHOTO_UPLOAD( "photoUpload", "Photo upload" )
	, PHOTOS( "photoLists", "Photo lists" )
	, PHOTO_CARD( "photoCard", "Photo card" )
	, COMMENTS( "photoComments", "Photo comments" )
	, PHOTO_VOTING( "photoVoting", "Photo voting" )
	, PHOTO_RATING( "photoRating", "Photo rating" )
	, RANK_VOTING( "rankVoting", "Voting for members' ranks in category" )
	, CACHE( "cache", "Cache" )
	, ADMIN( "admin", "Admin" )
	, EMAILING( "emailing", "Emailing" )
	;

	private final String key;
	private final String name;

	private ConfigurationTab( final String key, final String name ) {
		this.key = key;
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public static ConfigurationTab getByKey( final String tab ) {
		for ( final ConfigurationTab configurationTab : ConfigurationTab.values() ) {
			if ( configurationTab.getKey().equals( tab ) ) {
				return configurationTab;
			}
		}

		return null;
	}
}
