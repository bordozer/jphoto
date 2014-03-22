package admin.controllers.jobs.edit.photosImport;

public enum GenreDiscEntry {

	ADVERTISING( "Advertising" )
	, ANIMALS( "Animals" )
	, CHILDREN( "Children" )
	, CITY( "City" )
	, DIGITAL_ART( "Digital art" )
	, GENRE( "Genre" )
	, GLAMOUR( "Glamour" )
	, HDR( "HDR" )
	, HUMOR( "Humor" )
	, LANDSCAPE( "Landscape" )
	, MACRO( "Macro" )
	, MODELS( "Models" )
	, NUDE( "Nude" )
	, OTHER( "Other" )
	, PORTRAIT( "Portrait" )
	, REPORTING( "Reporting" )
	, SPORT( "Sport" )
	, STILL( "Still" )
	, TRAVELLING( "Travelling" )
	, UNDERWATER( "Underwater" )
	, WEDDING( "Wedding" )
	, WALLPAPERS( "Wallpapers" )
	;

	private final String name;

	GenreDiscEntry( final String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static GenreDiscEntry getByName( final String genreName ) {
		for ( final GenreDiscEntry genreDiscEntry : GenreDiscEntry.values() ) {
			if ( genreDiscEntry.getName().equals( genreName ) ) {
				return genreDiscEntry;
			}
		}

		throw new IllegalArgumentException( String.format( "GenreDiscEntry does not exist for name %s", genreName ) );
	}
}
