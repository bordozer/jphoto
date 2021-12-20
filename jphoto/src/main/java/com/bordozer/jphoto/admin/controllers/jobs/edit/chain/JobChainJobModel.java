package com.bordozer.jphoto.admin.controllers.jobs.edit.chain;

import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractAdminJobModel;
import com.bordozer.jphoto.admin.jobs.general.SavedJob;

import java.util.List;

public class JobChainJobModel extends AbstractAdminJobModel {

    public static final String SELECTED_SAVED_JOBS_IDS_FORM_CONTROL = "selectedSavedJobsIds";
    public static final String JOB_RUN_MODE_ID_FORM_CONTROL = "jobRunModeId";
    public static final String BREAK_CHAIN_EXECUTION_IF_ERROR_FORM_CONTROL = "breakChainExecutionIfError";

    private List<SavedJob> savedJobs;
    private List<String> selectedSavedJobsIds;

    private int jobRunModeId;
    private String breakChainExecutionIfError;

    public void setSavedJobs(final List<SavedJob> savedJobs) {
        this.savedJobs = savedJobs;
    }

    public List<SavedJob> getSavedJobs() {
        return savedJobs;
    }

    public List<String> getSelectedSavedJobsIds() {
        return selectedSavedJobsIds;
    }

    public void setSelectedSavedJobsIds(final List<String> selectedSavedJobsIds) {
        this.selectedSavedJobsIds = selectedSavedJobsIds;
    }

    public int getJobRunModeId() {
        return jobRunModeId;
    }

    public void setJobRunModeId(final int jobRunModeId) {
        this.jobRunModeId = jobRunModeId;
    }

    public String getBreakChainExecutionIfError() {
        return breakChainExecutionIfError;
    }

    public void setBreakChainExecutionIfError(final String breakChainExecutionIfError) {
        this.breakChainExecutionIfError = breakChainExecutionIfError;
    }
}
