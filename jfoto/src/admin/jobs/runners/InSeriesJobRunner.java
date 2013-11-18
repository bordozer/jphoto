package admin.jobs.runners;

import admin.jobs.entries.AbstractJob;
import core.log.LogHelper;

public class InSeriesJobRunner extends AbstractJobRunner {

	@Override
	public void runJob( final AbstractJob job ) {
		final LogHelper log = new LogHelper( InSeriesJobRunner.class );

		log.debug( String.format( "Job #%d is executing", job.getJobId() ) );
		job.start();
		log.debug( String.format( "Job #%d is finished", job.getJobId() ) );

		final AbstractJob parentJob = job.getParentJob();
		if ( parentJob != null ) {
			log.debug( String.format( "Job #%d is notifying parent job #%d than it can run another job", job.getJobId(), parentJob.getJobId() ) );
			parentJob.notifyAll();
		}
	}
}
