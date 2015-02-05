package admin.services.jobs;

import admin.jobs.enums.JobExecutionStatus;
import core.interfaces.BaseEntityService;
import core.interfaces.IdsSqlSelectable;

import java.util.Date;
import java.util.List;

public interface JobExecutionHistoryService extends BaseEntityService<JobExecutionHistoryEntry>, IdsSqlSelectable {

	List<JobExecutionHistoryEntry> getJobExecutionHistoryEntries();

	void setJobExecutionHistoryEntryStatus( final int jobId, final String message, final JobExecutionStatus status );

	void updateCurrentJobStep( final int jobExecutionHistoryId, final int currentJobStep );

	void updateTotalJobSteps( final int jobExecutionHistoryId, final int totalJobSteps );

	void deleteOldHistoryEntries( final int leaveQty );

	List<Integer> getEntriesIdsOlderThen( final Date timeFrame, final List<JobExecutionStatus> jobExecutionStatusesToDelete );

	void deleteEntriesOlderThen( final Date timeFrame, final List<JobExecutionStatus> jobExecutionStatusesToDelete );

	void delete( final List<Integer> ids );

	int getJobExecutionLogLength();

	List<JobExecutionHistoryEntry> getActiveJobs();
}
