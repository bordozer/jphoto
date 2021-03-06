package com.bordozer.jphoto.admin.services.scheduler;

import com.bordozer.jphoto.admin.services.jobs.JobExecutionService;
import com.bordozer.jphoto.admin.services.jobs.SavedJobService;
import com.bordozer.jphoto.admin.services.scheduler.triggers.AbstractJobTrigger;
import com.bordozer.jphoto.admin.services.scheduler.triggers.ExecutionTaskTriggerFactory;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.executiontasks.AbstractExecutionTask;
import com.bordozer.jphoto.core.general.scheduler.SchedulerTask;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bordozer.jphoto.admin.services.scheduler.triggers.ExecutionTaskTriggerFactory.getTriggerIdentity;
import static org.quartz.JobBuilder.newJob;

@Service("scheduledTasksExecutionService")
public class ScheduledTasksExecutionServiceImpl implements ScheduledTasksExecutionService {

    private final LogHelper log = new LogHelper();

    public static final String SCHEDULED_TASK_JOB_GROUP = "ScheduledTaskJobGroup";

    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private SavedJobService savedJobService;
    @Autowired
    private JobExecutionService jobExecutionService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private MyScheduler scheduler;
    @Autowired
    private DateUtilsService dateUtilsService;

    @Value("${app.scheduler.enabled}")
    private String schedulerEnabled;

    // TODO: init method
    public void loadSchedulerTasksAndStartScheduler() throws SchedulerException {

        loadSchedulerTasks();

        if (!Boolean.parseBoolean(schedulerEnabled)) {
            log.info("********************************************");
            log.info("*        THE SCHEDULER IS DISABLED         *");
            log.info("********************************************");

            return;
        }

        if (!configurationService.getBoolean(ConfigurationKey.SYSTEM_AUTO_START_SCHEDULER)) {
            log.info("********************************************");
            log.info("* THE SCHEDULER: AUTOSTART IS SWITCHED OFF *");
            log.info("********************************************");

            return;
        }

        scheduler.start();
    }

    @Override
    public void start() throws SchedulerException {
        // When you call start() after standby(), any misfires, which appear while standby, will be ignored.
        scheduler.start();
    }

    @Override
    public void standby() throws SchedulerException {
        // When you call start() after standby(), any misfires, which appear while standby, will be ignored.
        scheduler.standby();
    }

	/*@Override
	public void pauseAll() throws SchedulerException {
		// When you call resumeAll() after pauseAll(), all misfires, which appear while scheduler was paused, will be applied.
		scheduler.pauseAll();
	}*/

	/*@Override
	public void resumeAll() throws SchedulerException {
		//	Resume (un-pause) all triggers - similar to calling resumeTriggerGroup(group) on every group.
		//	If any Trigger missed one or more fire-times, then the Trigger's misfire instruction will be applied.
		scheduler.resumeAll(); // call after pauseAll()
	}*/

    @Override
    public boolean isRunning() throws SchedulerException {
        return scheduler.isRunning();
    }

    @Override
    public boolean isInStandbyMode() throws SchedulerException {
        return scheduler.isInStandbyMode();
    }

    @Override
    public boolean isTaskScheduled(final int schedulerTaskId) throws SchedulerException {
        final List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(getJobKey(schedulerTaskId));

        return triggersOfJob != null && triggersOfJob.size() > 0;
    }

    @Override
    public void scheduleTask(final int schedulerTaskId) throws SchedulerException {
        scheduleTask(schedulerService.load(schedulerTaskId));
    }

    @Override
    public void scheduleTask(final SchedulerTask schedulerTask) throws SchedulerException {

        final int schedulerTaskId = schedulerTask.getId();
        final int savedJobId = schedulerTask.getSavedJobId();// what to do

        final AbstractExecutionTask executionTask = schedulerTask.getExecutionTask(); // when to do

        final ExecutionTaskTriggerFactory triggerFactory = new ExecutionTaskTriggerFactory(schedulerTaskId, executionTask, dateUtilsService);
        final AbstractJobTrigger jobTrigger = triggerFactory.getJobTrigger();
        if (jobTrigger == null || jobTrigger.skipSchedulingJob()) {
            return;
        }

        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(SchedulerJobRunContainer.KEY_SCHEDULER_TASK, schedulerTask);
        jobDataMap.put(SchedulerJobRunContainer.KEY_JOB_EXECUTION_SERVICE, jobExecutionService);
        jobDataMap.put(SchedulerJobRunContainer.KEY_DATE_UTILS_SERVICE, dateUtilsService);
        jobDataMap.put(SchedulerJobRunContainer.KEY_SAVED_JOB_SERVICE, savedJobService);
        jobDataMap.put(SchedulerJobRunContainer.KEY_SAVED_JOB_ID, savedJobId);

        final JobDetail schedulerJob = newJob(SchedulerJobRunContainer.class)
                .withIdentity(getJobKey(schedulerTask.getId()))
                .usingJobData(jobDataMap)
                .build();

        final Trigger trigger = jobTrigger.getTrigger();
        scheduler.scheduleJob(schedulerJob, trigger);

        log.info(String.format("Scheduler task #%d is scheduled. Next firing time is %s"
                , schedulerTaskId, dateUtilsService.formatDateTime(trigger.getFireTimeAfter(dateUtilsService.getCurrentTime()))));
    }

    @Override
    public void unScheduleTask(final int schedulerTaskId) throws SchedulerException {
        scheduler.unscheduleJob(new TriggerKey(getTriggerIdentity(schedulerTaskId)));
        log.info(String.format("Scheduler task #%s has been unscheduled", schedulerTaskId));
    }

    @Override
    public boolean deleteJob(final SchedulerTask schedulerTask) throws SchedulerException {
        return deleteJob(schedulerTask.getId());
    }

    @Override
    public boolean deleteJob(final int schedulerTaskId) throws SchedulerException {
        log.info(String.format("Scheduler job for task #%s has been deleted from scheduler", schedulerTaskId));
        return scheduler.deleteJob(getJobKey(schedulerTaskId));
    }

    @Override
    public void reScheduleTask(final SchedulerTask schedulerTask) throws SchedulerException {
        final int schedulerTaskId = schedulerTask.getId();

        unScheduleTask(schedulerTaskId);

        deleteJob(schedulerTask);

        scheduleTask(schedulerTask);
    }

    private JobKey getJobKey(final int schedulerTaskId) {
        return new JobKey(ExecutionTaskTriggerFactory.getJobIdentity(schedulerTaskId), SCHEDULED_TASK_JOB_GROUP);
    }

    private void loadSchedulerTasks() throws SchedulerException {

        log.debug("===========================");
        log.debug("= Loading Scheduler Tasks =");
        log.debug("===========================");

        for (final SchedulerTask schedulerTask : schedulerService.loadAll()) {
            scheduleTask(schedulerTask);
        }
    }
}
