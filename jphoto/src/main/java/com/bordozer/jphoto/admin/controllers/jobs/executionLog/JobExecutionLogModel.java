package com.bordozer.jphoto.admin.controllers.jobs.executionLog;

import com.bordozer.jphoto.admin.jobs.entries.AbstractJob;

import java.util.List;

public class JobExecutionLogModel {

    private AbstractJob job;

    private List<String> jobRuntimeLogsMessages;
    private boolean jobNotFoundError;

    public AbstractJob getJob() {
        return job;
    }

    public void setJob(final AbstractJob job) {
        this.job = job;
    }

    public boolean isJobNotFoundError() {
        return jobNotFoundError;
    }

    public void setJobNotFoundError(final boolean jobNotFoundError) {
        this.jobNotFoundError = jobNotFoundError;
    }

    public List<String> getJobRuntimeLogsMessages() {
        return jobRuntimeLogsMessages;
    }

    public void setJobRuntimeLogsMessages(final List<String> jobRuntimeLogsMessages) {
        this.jobRuntimeLogsMessages = jobRuntimeLogsMessages;
    }
}
