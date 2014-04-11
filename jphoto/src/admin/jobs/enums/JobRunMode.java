package admin.jobs.enums;

public enum JobRunMode {

	SERIALLY( 1, "Serially" )
	, PARALLEL( 2, "Parallel" )
	;

	private final int id;
	private final String name;

	private JobRunMode( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static JobRunMode getById( int id ) {
		for ( JobRunMode jobRunMode : JobRunMode.values() ) {
			if ( jobRunMode.getId() == id ) {
				return jobRunMode;
			}
		}

		return null;
	}
}
