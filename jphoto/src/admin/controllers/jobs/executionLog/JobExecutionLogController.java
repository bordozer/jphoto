package admin.controllers.jobs.executionLog;

import admin.jobs.entries.AbstractJob;
import admin.jobs.general.JobRuntimeLog;
import admin.services.jobs.JobExecutionService;
import core.context.EnvironmentContext;
import core.services.translator.Language;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping( "jobs/execution" )
public class JobExecutionLogController {

	public static final String JOB_MODEL_NAME = "jobExecutionLogModel";

	private static final String VIEW = "admin/jobs/executionLog/JobExecutionLog";

	private static final int LOG_MESSAGE_QTY = 100;

	@Autowired
	private JobExecutionService jobExecutionService;

	@Autowired
	private DateUtilsService dateUtilsService;

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

		final List<JobRuntimeLog> jobRuntimeLogs = job.getJobRuntimeLogs();
		Collections.sort( jobRuntimeLogs, new Comparator<JobRuntimeLog>() {
			@Override
			public int compare( final JobRuntimeLog o1, final JobRuntimeLog o2 ) {
				return o2.getJobRuntimeLogEntryTime().compareTo( o1.getJobRuntimeLogEntryTime() );
			}
		} );

		final Language language = EnvironmentContext.getLanguage();

		final List<String> jobRuntimeLogsMessage = newArrayList();
		for ( final JobRuntimeLog jobRuntimeLog : jobRuntimeLogs ) {
			final String translation = jobRuntimeLog.getTranslatableMessage().build( language );
			jobRuntimeLogsMessage.add( String.format( "%s %s", dateUtilsService.formatTime( jobRuntimeLog.getJobRuntimeLogEntryTime() ), translation ) );
		}

		model.setJobRuntimeLogsMessages( jobRuntimeLogsMessage.size() < LOG_MESSAGE_QTY ? jobRuntimeLogsMessage : jobRuntimeLogsMessage.subList( 0, LOG_MESSAGE_QTY ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "//log/" )
	public String showJobNotFound( final @ModelAttribute( JOB_MODEL_NAME ) JobExecutionLogModel model ) {

		model.setJobNotFoundError( true );

		return VIEW;
	}
}
