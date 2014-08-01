package core.general.photo;

public enum PhotoImageSourceType {

	FILE( 1 ),
	WEB( 2 );

	private final int id;

	PhotoImageSourceType( final int id ) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static PhotoImageSourceType getById( final int id ) {
		for ( final PhotoImageSourceType photoImageSourceType : values() ) {
			if ( photoImageSourceType.getId() == id ) {
				return photoImageSourceType;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal PhotoImageSourceTypeId: %D", id ) );
	}
}
