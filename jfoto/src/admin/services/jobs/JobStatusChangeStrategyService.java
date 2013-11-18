package admin.services.jobs;

import admin.jobs.entries.AbstractJob;
import admin.jobs.general.JobStatusChangeStrategy;

public interface JobStatusChangeStrategyService {

	JobStatusChangeStrategy doneSuccessfully( final AbstractJob job );

	JobStatusChangeStrategy inProgress( final AbstractJob job );

	JobStatusChangeStrategy error( final AbstractJob job, final String exceptionMessage );

	JobStatusChangeStrategy stoppedByUser( final AbstractJob job );

	JobStatusChangeStrategy stoppedByParentJob( final AbstractJob job, final AbstractJob parentJob );

	JobStatusChangeStrategy cancelledByParentJob( final AbstractJob job, final AbstractJob parentJob );

	JobStatusChangeStrategy cancelledByErrorInChild( final AbstractJob job, final AbstractJob failedChildJob );

	JobStatusChangeStrategy cancelledByErrorInParent( final AbstractJob job, final AbstractJob failedParentJob );
}
