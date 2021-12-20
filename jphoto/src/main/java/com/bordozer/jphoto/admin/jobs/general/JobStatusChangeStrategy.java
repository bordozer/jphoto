package com.bordozer.jphoto.admin.jobs.general;

import com.bordozer.jphoto.admin.jobs.entries.AbstractJob;
import com.bordozer.jphoto.admin.jobs.enums.JobExecutionStatus;
import com.bordozer.jphoto.core.log.LogHelper;
import org.apache.commons.lang3.StringUtils;

public abstract class JobStatusChangeStrategy {

    private final AbstractJob job;

    protected abstract void performCustomActions();

    public abstract JobExecutionStatus getJobStatus();

    public JobStatusChangeStrategy(final AbstractJob job) {
        this.job = job;
    }

    final public void changeStatus() {
        changeStatus(getMessage());
    }

    final public void changeStatus(final String message) {

        if (!job.isActive()) {
            return;
        }

        final GenerationMonitor monitor = job.getGenerationMonitor();
        monitor.setStatus(getJobStatus());
        monitor.setErrorMessage(message);

        performCustomActions();

        final LogHelper log = new LogHelper();
        log.info(message);
    }

    final public AbstractJob getJob() {
        return job;
    }

    public String getMessage() {
        return StringUtils.EMPTY;
    }
}
