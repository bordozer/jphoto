package admin.controllers.jobs.executionLog;

import admin.jobs.entries.AbstractJob;
import admin.jobs.general.JobExecutionFinalMessage;

import java.util.List;

public class JobExecutionLogModel {

	private AbstractJob job;

	private List<JobExecutionFinalMessage> jobExecutionFinalMessages;
	private boolean jobNotFoundError;

	public AbstractJob getJob() {
		return job;
	}

	public void setJob( final AbstractJob job ) {
		this.job = job;
	}

	public boolean isJobNotFoundError() {
		return jobNotFoundError;
	}

	public void setJobNotFoundError( final boolean jobNotFoundError ) {
		this.jobNotFoundError = jobNotFoundError;
	}

	public List<JobExecutionFinalMessage> getJobExecutionFinalMessages() {
		return jobExecutionFinalMessages;
	}

	public void setJobExecutionFinalMessages( final List<JobExecutionFinalMessage> jobExecutionFinalMessages ) {
		this.jobExecutionFinalMessages = jobExecutionFinalMessages;
	}
}
