package core.general.photo;

public enum PhotoImageSourceType {

	FILE( 1, "PhotoImageSourceType: file" )
	, WEB( 2, "PhotoImageSourceType: web" );

	private final int id;
	private final String description;

	PhotoImageSourceType( final int id, final String description ) {
		this.id = id;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public static PhotoImageSourceType getById( int id ) {
		for ( PhotoImageSourceType importSource : PhotoImageSourceType.values() ) {
			if ( importSource.getId() == id ) {
				return importSource;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal PhotoImageSourceType: %d", id ) );
	}
}
