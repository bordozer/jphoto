package admin.controllers.jobs.edit.action;

public enum PhotoStrategyType {

	PERFECT( 1 )
	, VERY_GOOD( 2 )
	, NORMAL( 3 )
	, COULD_BE_BETTER( 4 )
	, COULD_BE_WORST( 5 )
	, UGLY_SHIT( 6 )
	;

	private final int id;

	private PhotoStrategyType( final int id ) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static PhotoStrategyType getById( final int id ) {
		for ( final PhotoStrategyType strategyType : PhotoStrategyType.values() ) {
			if ( strategyType.getId() == id ) {
				return strategyType;
			}
		}

		throw new IllegalArgumentException( String.format( "Wrong PhotoStrategyType id: %d", id ) );
	}
}
