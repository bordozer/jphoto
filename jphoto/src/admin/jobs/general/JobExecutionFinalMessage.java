package admin.jobs.general;

import java.util.Date;

public class JobExecutionFinalMessage {

	private final String finalMessage;
	private final Date finalMessageTime;

	public JobExecutionFinalMessage( final String finalMessage, final Date finalMessageTime ) {
		this.finalMessage = finalMessage;
		this.finalMessageTime = finalMessageTime;
	}

	public String getFinalMessage() {
		return finalMessage;
	}

	public Date getFinalMessageTime() {
		return finalMessageTime;
	}
}
