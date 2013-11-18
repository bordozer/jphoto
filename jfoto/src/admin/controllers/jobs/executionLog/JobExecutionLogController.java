package admin.controllers.jobs.executionLog;

import admin.jobs.entries.AbstractJob;
import admin.services.jobs.JobExecutionService;
import admin.jobs.general.JobExecutionFinalMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping( "jobs/execution" )
public class JobExecutionLogController {

	public static final String JOB_MODEL_NAME = "jobExecutionLogModel";

	private static final String VIEW = "admin/jobs/executionLog/JobExecutionLog";

	private static final int LOG_MESSAGE_QTY = 100;

	@Autowired
	private JobExecutionService jobExecutionService;

	@ModelAttribute( JOB_MODEL_NAME )
	public JobExecutionLogModel initModel() {
		return new JobExecutionLogModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{jobId}/log/" )
	public String showLog( final @PathVariable( "jobId" ) int jobId, final @ModelAttribute( JOB_MODEL_NAME ) JobExecutionLogModel model ) {

		final AbstractJob job = jobExecutionService.getActiveJob( jobId );
		model.setJob( job );

		if ( job == null ) {
			return VIEW;
		}

		final List<JobExecutionFinalMessage> jobExecutionFinalMessages = job.getJobExecutionFinalMessages();
		Collections.sort( jobExecutionFinalMessages, new Comparator<JobExecutionFinalMessage>() {
			@Override
			public int compare( final JobExecutionFinalMessage o1, final JobExecutionFinalMessage o2 ) {
				return o2.getFinalMessageTime().compareTo( o1.getFinalMessageTime() );
			}
		} );

		model.setJobExecutionFinalMessages( jobExecutionFinalMessages.size() < LOG_MESSAGE_QTY ? jobExecutionFinalMessages : jobExecutionFinalMessages.subList( 0, LOG_MESSAGE_QTY ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "//log/" )
	public String showJobNotFound( final @ModelAttribute( JOB_MODEL_NAME ) JobExecutionLogModel model ) {

		model.setJobNotFoundError( true );

		return VIEW;
	}
}
