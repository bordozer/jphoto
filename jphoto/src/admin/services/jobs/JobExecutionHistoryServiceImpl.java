package admin.services.jobs;

import admin.jobs.enums.JobExecutionStatus;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class JobExecutionHistoryServiceImpl implements JobExecutionHistoryService {

	@Autowired
	private JobExecutionHistoryDao jobExecutionHistoryDao;

	@Override
	public List<JobExecutionHistoryEntry> getJobExecutionHistoryEntries() {
		return jobExecutionHistoryDao.getJobExecutionHistoryEntries();
	}

	@Override
	public void setJobExecutionHistoryEntryStatus( final int jobId, final String message, final JobExecutionStatus status ) {
		jobExecutionHistoryDao.setJobExecutionHistoryEntryStatus( jobId, message, status );
	}

	@Override
	public boolean save( final JobExecutionHistoryEntry entry ) {
		return jobExecutionHistoryDao.saveToDB( entry );
	}

	@Override
	public JobExecutionHistoryEntry load( final int jobExecutionHistoryEntryId ) {
		final JobExecutionHistoryEntry executionHistoryEntry = jobExecutionHistoryDao.load( jobExecutionHistoryEntryId );

		if ( executionHistoryEntry == null ) {
			return null;
		}

		executionHistoryEntry.setParametersMap( getJobParametersMap( jobExecutionHistoryEntryId ) );

		return executionHistoryEntry;
	}

	@Override
	public boolean delete( final int entryId ) {
		jobExecutionHistoryDao.deleteJobParameters( entryId );
		return jobExecutionHistoryDao.delete( entryId );
	}

	@Override
	public void updateCurrentJobStep( final int jobExecutionHistoryId, final int currentJobStep ) {
		jobExecutionHistoryDao.updateCurrentJobStep( jobExecutionHistoryId, currentJobStep );
	}

	@Override
	public void updateTotalJobSteps( final int jobExecutionHistoryId, final int totalJobSteps ) {
		jobExecutionHistoryDao.updateTotalJobSteps( jobExecutionHistoryId, totalJobSteps );
	}

	@Override
	public void deleteOldHistoryEntries( final int leaveQty ) {
		jobExecutionHistoryDao.deleteOldHistoryEntries( leaveQty );
	}

	@Override
	public List<Integer> getEntriesIdsOlderThen( final Date timeFrame, final List<JobExecutionStatus> jobExecutionStatusesToDelete ) {
		return jobExecutionHistoryDao.getEntriesIdsOlderThen( timeFrame, jobExecutionStatusesToDelete );
	}

	@Override
	public void deleteEntriesOlderThen( final Date timeFrame, final List<JobExecutionStatus> jobExecutionStatusesToDelete ) {
		jobExecutionHistoryDao.deleteEntriesOlderThen( timeFrame, jobExecutionStatusesToDelete );
	}

	@Override
	public SqlSelectIdsResult load( final SqlIdsSelectQuery selectIdsQuery ) {
		return jobExecutionHistoryDao.load( selectIdsQuery );
	}

	@Override
	public void delete( final List<Integer> ids ) {
		jobExecutionHistoryDao.delete( ids );
	}

	@Override
	public int getJobExecutionLogLength() {
		return jobExecutionHistoryDao.getJobExecutionLogLength();
	}

	@Override
	public List<JobExecutionHistoryEntry> getActiveJobs() {
		return jobExecutionHistoryDao.getActiveJobs();
	}

	private Map<SavedJobParameterKey, CommonProperty> getJobParametersMap( final int jobExecutionHistoryId ) {
		final Map<SavedJobParameterKey, CommonProperty> jobParameters = jobExecutionHistoryDao.getJobParameters( jobExecutionHistoryId );

		for ( final SavedJobParameterKey savedJobParameterKey : SavedJobParameterKey.values() ) {
			if ( ! jobParameters.containsKey( savedJobParameterKey ) ) {
				jobParameters.put( savedJobParameterKey, CommonProperty.getEmptyProperty( savedJobParameterKey.getId() ) );
			}
		}

		return jobParameters;
	}

	@Override
	public boolean exists( final int entryId ) {
		return jobExecutionHistoryDao.exists( entryId );
	}

	@Override
	public boolean exists( final JobExecutionHistoryEntry entry ) {
		return jobExecutionHistoryDao.exists( entry );
	}
}
