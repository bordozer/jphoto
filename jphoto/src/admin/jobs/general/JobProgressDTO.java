package admin.jobs.general;

public class JobProgressDTO {

	private int current;
	private int total;
	private int percent;
	private int jobStatusId;
	private boolean jobActive;
	private String jobExecutionDuration;

	public int getCurrent() {
		return current;
	}

	public void setCurrent( final int current ) {
		this.current = current;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal( final int total ) {
		this.total = total;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent( final int percent ) {
		this.percent = percent;
	}

	public int getJobStatusId() {
		return jobStatusId;
	}

	public void setJobStatusId( final int jobStatusId ) {
		this.jobStatusId = jobStatusId;
	}

	public boolean isJobActive() {
		return jobActive;
	}

	public void setJobActive( final boolean jobActive ) {
		this.jobActive = jobActive;
	}

	public String getJobExecutionDuration() {
		return jobExecutionDuration;
	}

	public void setJobExecutionDuration( final String jobExecutionDuration ) {
		this.jobExecutionDuration = jobExecutionDuration;
	}
}
