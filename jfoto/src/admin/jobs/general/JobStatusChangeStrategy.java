package admin.jobs.general;

import admin.jobs.entries.AbstractJob;
import admin.jobs.enums.JobExecutionStatus;
import core.log.LogHelper;
import org.apache.commons.lang.StringUtils;

public abstract class JobStatusChangeStrategy {

	private final AbstractJob job;

	protected abstract void performCustomActions();

	public abstract JobExecutionStatus getJobStatus();

	public JobStatusChangeStrategy( final AbstractJob job ) {
		this.job = job;
	}

	final public void changeStatus() {
		changeStatus( getMessage() );
	}

	final public void changeStatus( final String message ) {

		if ( !job.isActive() ) {
			return;
		}

		final GenerationMonitor monitor = job.getGenerationMonitor();
		monitor.setStatus( getJobStatus() );
		monitor.setErrorMessage( message );

		performCustomActions();

		final LogHelper log = new LogHelper( JobStatusChangeStrategy.class );
		log.info( message );
	}

	final public AbstractJob getJob() {
		return job;
	}

	public String getMessage() {
		return StringUtils.EMPTY;
	}
}
