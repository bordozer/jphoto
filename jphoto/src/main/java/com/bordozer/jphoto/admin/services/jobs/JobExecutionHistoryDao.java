package com.bordozer.jphoto.admin.services.jobs;

import com.bordozer.jphoto.admin.jobs.enums.JobExecutionStatus;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.core.services.dao.BaseEntityDao;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface JobExecutionHistoryDao extends BaseEntityDao<JobExecutionHistoryEntry> {

    List<JobExecutionHistoryEntry> getJobExecutionHistoryEntries();

    void setJobExecutionHistoryEntryStatus(final int jobId, final String message, final JobExecutionStatus status);

    void updateCurrentJobStep(final int jobExecutionHistoryId, final int currentJobStep);

    void updateTotalJobSteps(final int jobExecutionHistoryId, final int totalJobSteps);

    boolean saveParameter(final int jobExecutionHistoryId, final SavedJobParameterKey key, final String value);

    Map<SavedJobParameterKey, CommonProperty> getJobParameters(final int jobExecutionHistoryId);

    boolean deleteJobParameters(final int jobExecutionHistoryId);

    void deleteOldHistoryEntries(final int leaveQty);

    List<Integer> getEntriesIdsOlderThen(final Date timeFrame, final List<JobExecutionStatus> jobExecutionStatusesToDelete);

    void deleteEntriesOlderThen(final Date timeFrame, final List<JobExecutionStatus> jobExecutionStatusesToDelete);

    void delete(final List<Integer> ids);

    int getJobExecutionLogLength();

    List<JobExecutionHistoryEntry> getActiveJobs();
}
