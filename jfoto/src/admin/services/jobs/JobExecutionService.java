package admin.services.jobs;

import admin.jobs.entries.AbstractJob;
import admin.jobs.general.GenerationMonitor;
import admin.jobs.general.JobProgressDTO;
import admin.jobs.general.JobStatusChangeStrategy;

import java.util.List;

public interface JobExecutionService {

	String BEAN_NAME = "jobExecutionService";

	void execute( final AbstractJob job );

	void changeJobStatus( final JobStatusChangeStrategy statusChangeStrategy );

	void changeJobStatus_Done( final AbstractJob job );

	void changeJobStatus_InProgress( final AbstractJob job );

	void breakJobExecutionChain( final AbstractJob failedJob, final JobStatusChangeStrategy statusChangeStrategy );

	void breakJobExecutionChain( final AbstractJob failedJob, final JobStatusChangeStrategy statusChangeStrategy, final boolean forceStopChild );

	void stopJobWithChildByUserDemand( final int jobId );

	void removeJobFromActiveList( final AbstractJob job ); // TODO: hide

	GenerationMonitor getMonitor( final AbstractJob job );

	JobProgressDTO getJobProgressAjax( final int jobId );

	List<AbstractJob> getActiveJobs();

	AbstractJob getActiveJob( final int jobId );

	void initJobServices( final AbstractJob job );
}
