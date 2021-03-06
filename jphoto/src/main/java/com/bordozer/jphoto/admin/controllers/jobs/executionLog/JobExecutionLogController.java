package com.bordozer.jphoto.admin.controllers.jobs.executionLog;

import com.bordozer.jphoto.admin.jobs.entries.AbstractJob;
import com.bordozer.jphoto.admin.jobs.general.JobRuntimeLog;
import com.bordozer.jphoto.admin.services.jobs.JobExecutionService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping("/admin/jobs/execution")
public class JobExecutionLogController {

    public static final String JOB_MODEL_NAME = "jobExecutionLogModel";

    private static final String VIEW = "admin/jobs/executionLog/JobExecutionLog";

    private static final int LOG_MESSAGE_QTY = 100;

    @Autowired
    private JobExecutionService jobExecutionService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @ModelAttribute(JOB_MODEL_NAME)
    public JobExecutionLogModel initModel() {
        return new JobExecutionLogModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{jobId}/log/")
    public String showLog(final @PathVariable("jobId") int jobId, final @ModelAttribute(JOB_MODEL_NAME) JobExecutionLogModel model) {

        final AbstractJob job = jobExecutionService.getActiveJob(jobId);
        model.setJob(job);

        if (job == null) {
            return VIEW;
        }

        final List<String> jobRuntimeLogsMessage = newArrayList();
        for (final JobRuntimeLog jobRuntimeLog : Lists.reverse(newArrayList(job.getJobRuntimeLogs()))) {
            final String translation = jobRuntimeLog.getTranslatableMessage().build(EnvironmentContext.getLanguage());
            jobRuntimeLogsMessage.add(String.format("%s &nbsp;&nbsp; %s", dateUtilsService.formatTime(jobRuntimeLog.getJobRuntimeLogEntryTime()), translation));
        }

        model.setJobRuntimeLogsMessages(jobRuntimeLogsMessage.size() < LOG_MESSAGE_QTY ? jobRuntimeLogsMessage : jobRuntimeLogsMessage.subList(0, LOG_MESSAGE_QTY));

        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "//log/")
    public String showJobNotFound(final @ModelAttribute(JOB_MODEL_NAME) JobExecutionLogModel model) {

        model.setJobNotFoundError(true);

        return VIEW;
    }
}
