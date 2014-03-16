package admin.jobs.enums;

import core.interfaces.Identifiable;

import java.util.EnumSet;

public enum JobExecutionStatus implements Identifiable {

	WAITING_FOR_START( 5, "Jobs are waiting for start", "hourglass.png" )
	, IN_PROGRESS( 1, "Jobs in progress", "progress.png" )
	, DONE( 2, "Jobs is performed successfully", "done.png" )
	, STOPPED_BY_USER( 3, "Jobs stopped by user", "stopped.png" )
	, STOPPED_BY_PARENT_JOB( 8, "Jobs that had already started but the execution was stopped because a user had stopped parent job", "stoppedByParentJob.png" )
	, STOPPED_BECAUSE_OF_ERROR_IN_CHILD( 6, "Chain jobs was stopped because of error in child job", "childError.png" )
	, CANCELLED_BY_PARENT_JOB( 9, "Jobs that have not started yet and execution is cancelled because a user had stopped parent job", "cancelledByParentJob.png" )
	, CANCELLED_BECAUSE_OF_ERROR_IN_PARENT( 7, "Jobs that have not started yet and execution was cancelled because of error in previous job in the chain", "parentError.png" )
	, ERROR( 4, "Finished with error", "error.png" ) // Exception in job
	;

	private final static EnumSet ACTIVE_STATUSES = EnumSet.of( IN_PROGRESS, WAITING_FOR_START );
	private final static EnumSet NOT_ACTIVE_STATUSES = EnumSet.of( DONE, ERROR, STOPPED_BY_USER, STOPPED_BECAUSE_OF_ERROR_IN_CHILD, CANCELLED_BECAUSE_OF_ERROR_IN_PARENT, STOPPED_BY_PARENT_JOB, CANCELLED_BY_PARENT_JOB );
	private final static EnumSet STOPPED_ERROR_OR_CANCELLED = EnumSet.of( ERROR, STOPPED_BECAUSE_OF_ERROR_IN_CHILD, CANCELLED_BECAUSE_OF_ERROR_IN_PARENT, STOPPED_BY_PARENT_JOB, CANCELLED_BY_PARENT_JOB );

	private final int id;
	private final String name;
	private final String icon;

	private JobExecutionStatus( final int id, final String name, final String icon ) {
		this.id = id;
		this.name = name;
		this.icon = icon;
	}

	public boolean isActive() {
		return ACTIVE_STATUSES.contains( this );
	}

	public boolean isNotActive() {
		return NOT_ACTIVE_STATUSES.contains( this );
	}

	public boolean isStoppedOrErrorOrCancelled() {
		return STOPPED_ERROR_OR_CANCELLED.contains( this );
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIcon() {
		return icon;
	}

	public static JobExecutionStatus getById( final int id ) {
		for ( final JobExecutionStatus executionStatus : JobExecutionStatus.values() ) {
			if ( executionStatus.getId() == id ) {
				return executionStatus;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal JobExecutionStatus id: #%d", id ) );
	}
}
