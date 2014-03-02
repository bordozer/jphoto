package core.general.executiontasks;

public enum ExecutionTaskType {
	ONCE( 1, "One time", "schedulerTaskOnce.png" )
	, PERIODICAL( 2, "Periodically", "schedulerTaskPeriodical.png" )
	, DAILY( 3, "Daily", "schedulerTaskDaily.png" )
	, MONTHLY( 4, "Monthly", "schedulerTaskMonthly.png" );

	private final int id;
	private final String name;
	private final String icon;

	private ExecutionTaskType( final int id, final String name, final String icon ) {
		this.id = id;
		this.name = name;
		this.icon = icon;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNameTranslated() {
		return name; // TODO: translate
	}

	public String getIcon() {
		return icon;
	}

	public static ExecutionTaskType getById( final int id ) {
		for ( final ExecutionTaskType executionTaskType : ExecutionTaskType.values() ) {
			if ( executionTaskType.getId() == id ) {
				return executionTaskType;
			}
		}

		throw new IllegalArgumentException( String.format( "Invalid index %s", id ) );
	}
}
