package admin.jobs.general;

import admin.jobs.enums.JobExecutionStatus;
import utils.NumberUtils;

public class GenerationMonitor {

	private int current;
	private int total;

	private JobExecutionStatus status;
	private String errorMessage;

	public GenerationMonitor() {
		status = JobExecutionStatus.DONE;
	}

	public void increment() {
		current++;
	}

	public void increment( final int count ) {
		current += count;
	}

	public void resetCounter() {
		current = 0;
	}

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

	public JobExecutionStatus getStatus() {
		return status;
	}

	public void setStatus( final JobExecutionStatus status ) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage( final String errorMessage ) {
		this.errorMessage = errorMessage;
	}

	public int getPercentage() {
		if ( current == 0 || total == 0 ) {
			return 0;
		}
		return NumberUtils.floor( 100 * current / total );
	}
}
