package core.general.restriction;

public enum RestrictionTimeUnit {

	HOUR( 1 )
	, DAY( 2 )
	, MONTH( 3 )
	, YEAR( 4 )
	;

	private final int id;

	RestrictionTimeUnit( final int id ) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static RestrictionTimeUnit getById( final int id ) {
		for ( final RestrictionTimeUnit configurationDataType : RestrictionTimeUnit.values() ) {
			if ( configurationDataType.getId() == id ) {
				return configurationDataType;
			}
		}
		return null;
	}
}
