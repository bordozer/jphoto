package admin.jobs.runners;

import admin.jobs.entries.AbstractJob;
import core.log.LogHelper;

public class AsSeparateThreadJobRunner extends AbstractJobRunner {

	@Override
	public void runJob( final AbstractJob job ) {

		final LogHelper log = new LogHelper( AsSeparateThreadJobRunner.class );

		log.debug( String.format( "Job #%d is executing", job.getJobId() ) );
		job.start();
		log.debug( String.format( "Job #%d is finished", job.getJobId() ) );

		/*new Thread() {
			@Override
			public void run() {
				final LogHelper log = new LogHelper( AsSeparateThreadJobRunner.class );

				final AbstractJob parentJob = job.getParentJob();
				if ( parentJob != null ) {
					log.debug( String.format( "Job #%d is notifying parent job #%d than it can run another job", job.getJobId(), parentJob.getJobId() ) );
					parentJob.notifyAll();
				}

				log.debug( String.format( "Job #%d is executing", job.getJobId() ) );
				job.initAndRunJob();
				log.debug( String.format( "Job #%d is finished", job.getJobId() ) );
			}
		}.start();*/
	}
}
