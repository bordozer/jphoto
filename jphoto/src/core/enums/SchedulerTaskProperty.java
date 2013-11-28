package core.enums;

public enum SchedulerTaskProperty {

	PROPERTY_IS_ACTIVE( 1 )
	, PROPERTY_SKIP_MISSED_EXECUTIONS( 3 )
	, PROPERTY_TASK_PERIOD( 4 )
	, PROPERTY_TASK_PERIOD_UNIT( 2 )
	, PROPERTY_DAILY_TASK_WEEKDAY_IDS( 5 )
	, PROPERTY_MONTHLY_TASK_MONTH_IDS( 6 )
	, PROPERTY_MONTHLY_TASK_DAY_OF_MONTH( 7 )
	, PROPERTY_START_TASK_DATE( 8 )
	, PROPERTY_END_TASK_DATE( 9 )
	, PROPERTY_TASK_HOURS( 10 )
	;

	private final int id;

	private SchedulerTaskProperty( final int id ) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static SchedulerTaskProperty getById( final int id ) {
		for ( final SchedulerTaskProperty upgradeTaskResult : SchedulerTaskProperty.values() ) {
			if ( upgradeTaskResult.getId() == id ) {
				return upgradeTaskResult;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal SchedulerTaskProperty id: %d", id ) );
	}
}
