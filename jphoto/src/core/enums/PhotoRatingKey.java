package core.enums;

public enum PhotoRatingKey {
	DAY( 1, "date" )
	, WEEK( 2, "week" )
	, MONTH( 3, "month" )
	, YEAR( 4, "year" )
	;

	private final int id;
	private final String name;

	private PhotoRatingKey( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static PhotoRatingKey getById( final int id ) {
		for ( final PhotoRatingKey upgradeTaskResult : PhotoRatingKey.values() ) {
			if ( upgradeTaskResult.getId() == id ) {
				return upgradeTaskResult;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal PhotoRatingKey id: %d", id ) );
	}
}
