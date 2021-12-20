package com.bordozer.jphoto.admin.services.scheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class MyScheduler implements Scheduler {

    final Scheduler scheduler;
    private boolean isRunning = false;

    public boolean isRunning() {
        return isRunning;
    }

    public MyScheduler() throws SchedulerException {
        this.scheduler = StdSchedulerFactory.getDefaultScheduler();
    }

    @Override
    public String getSchedulerName() throws SchedulerException {
        return scheduler.getSchedulerName();
    }

    @Override
    public String getSchedulerInstanceId() throws SchedulerException {
        return scheduler.getSchedulerInstanceId();
    }

    @Override
    public SchedulerContext getContext() throws SchedulerException {
        return scheduler.getContext();
    }

    @Override
    public synchronized void start() throws SchedulerException {
        scheduler.start();
        isRunning = true;
    }

    @Override
    public void startDelayed(final int i) throws SchedulerException {
        scheduler.startDelayed(i);
    }

    @Override
    public boolean isStarted() throws SchedulerException {
        return scheduler.isStarted();
    }

    @Override
    public synchronized void standby() throws SchedulerException {
        scheduler.standby();
        isRunning = false;
    }

    @Override
    public boolean isInStandbyMode() throws SchedulerException {
        return scheduler.isInStandbyMode();
    }

    @Override
    public synchronized void shutdown() throws SchedulerException {
        scheduler.shutdown();
        isRunning = false;
    }

    @Override
    public void shutdown(final boolean b) throws SchedulerException {
        scheduler.shutdown(b);
    }

    @Override
    public boolean isShutdown() throws SchedulerException {
        return scheduler.isShutdown();
    }

    @Override
    public SchedulerMetaData getMetaData() throws SchedulerException {
        return scheduler.getMetaData();
    }

    @Override
    public List<JobExecutionContext> getCurrentlyExecutingJobs() throws SchedulerException {
        return scheduler.getCurrentlyExecutingJobs();
    }

    @Override
    public void setJobFactory(final JobFactory jobFactory) throws SchedulerException {
        scheduler.setJobFactory(jobFactory);
    }

    @Override
    public ListenerManager getListenerManager() throws SchedulerException {
        return scheduler.getListenerManager();
    }

    @Override
    public Date scheduleJob(final JobDetail jobDetail, final Trigger trigger) throws SchedulerException {
        return scheduler.scheduleJob(jobDetail, trigger);
    }

    @Override
    public Date scheduleJob(final Trigger trigger) throws SchedulerException {
        return scheduler.scheduleJob(trigger);
    }

    @Override
    public void scheduleJobs(final Map<JobDetail, Set<? extends Trigger>> jobDetailSetMap, final boolean b) throws SchedulerException {
        scheduler.scheduleJobs(jobDetailSetMap, b);
    }

    @Override
    public void scheduleJob(final JobDetail jobDetail, final Set<? extends Trigger> triggers, final boolean b) throws SchedulerException {
        scheduler.scheduleJob(jobDetail, triggers, b);
    }

    @Override
    public boolean unscheduleJob(final TriggerKey triggerKey) throws SchedulerException {
        return scheduler.unscheduleJob(triggerKey);
    }

    @Override
    public boolean unscheduleJobs(final List<TriggerKey> triggerKeys) throws SchedulerException {
        return scheduler.unscheduleJobs(triggerKeys);
    }

    @Override
    public Date rescheduleJob(final TriggerKey triggerKey, final Trigger trigger) throws SchedulerException {
        return scheduler.rescheduleJob(triggerKey, trigger);
    }

    @Override
    public void addJob(final JobDetail jobDetail, final boolean b) throws SchedulerException {
        scheduler.addJob(jobDetail, b);
    }

    @Override
    public void addJob(final JobDetail jobDetail, final boolean b, final boolean b1) throws SchedulerException {
        scheduler.addJob(jobDetail, b, b1);
    }

    @Override
    public boolean deleteJob(final JobKey jobKey) throws SchedulerException {
        return scheduler.deleteJob(jobKey);
    }

    @Override
    public boolean deleteJobs(final List<JobKey> jobKeys) throws SchedulerException {
        return scheduler.deleteJobs(jobKeys);
    }

    @Override
    public void triggerJob(final JobKey jobKey) throws SchedulerException {
        scheduler.triggerJob(jobKey);
    }

    @Override
    public void triggerJob(final JobKey jobKey, final JobDataMap jobDataMap) throws SchedulerException {
        scheduler.triggerJob(jobKey, jobDataMap);
    }

    @Override
    public void pauseJob(final JobKey jobKey) throws SchedulerException {
        scheduler.pauseJob(jobKey);
    }

    @Override
    public void pauseJobs(final GroupMatcher<JobKey> jobKeyGroupMatcher) throws SchedulerException {
        scheduler.pauseJobs(jobKeyGroupMatcher);
    }

    @Override
    public void pauseTrigger(final TriggerKey triggerKey) throws SchedulerException {
        scheduler.pauseTrigger(triggerKey);
    }

    @Override
    public void pauseTriggers(final GroupMatcher<TriggerKey> triggerKeyGroupMatcher) throws SchedulerException {
        scheduler.pauseTriggers(triggerKeyGroupMatcher);
    }

    @Override
    public void resumeJob(final JobKey jobKey) throws SchedulerException {
        scheduler.resumeJob(jobKey);
    }

    @Override
    public void resumeJobs(final GroupMatcher<JobKey> jobKeyGroupMatcher) throws SchedulerException {
        scheduler.resumeJobs(jobKeyGroupMatcher);
    }

    @Override
    public void resumeTrigger(final TriggerKey triggerKey) throws SchedulerException {
        scheduler.resumeTrigger(triggerKey);
    }

    @Override
    public void resumeTriggers(final GroupMatcher<TriggerKey> triggerKeyGroupMatcher) throws SchedulerException {
        scheduler.resumeTriggers(triggerKeyGroupMatcher);
    }

    @Override
    public synchronized void pauseAll() throws SchedulerException {
        scheduler.pauseAll();
        isRunning = false;
    }

    @Override
    public synchronized void resumeAll() throws SchedulerException {
        scheduler.resumeAll();
        isRunning = true;
    }

    @Override
    public List<String> getJobGroupNames() throws SchedulerException {
        return scheduler.getJobGroupNames();
    }

    @Override
    public Set<JobKey> getJobKeys(final GroupMatcher<JobKey> jobKeyGroupMatcher) throws SchedulerException {
        return scheduler.getJobKeys(jobKeyGroupMatcher);
    }

    @Override
    public List<? extends Trigger> getTriggersOfJob(final JobKey jobKey) throws SchedulerException {
        return scheduler.getTriggersOfJob(jobKey);
    }

    @Override
    public List<String> getTriggerGroupNames() throws SchedulerException {
        return scheduler.getTriggerGroupNames();
    }

    @Override
    public Set<TriggerKey> getTriggerKeys(final GroupMatcher<TriggerKey> triggerKeyGroupMatcher) throws SchedulerException {
        return scheduler.getTriggerKeys(triggerKeyGroupMatcher);
    }

    @Override
    public Set<String> getPausedTriggerGroups() throws SchedulerException {
        return scheduler.getPausedTriggerGroups();
    }

    @Override
    public JobDetail getJobDetail(final JobKey jobKey) throws SchedulerException {
        return scheduler.getJobDetail(jobKey);
    }

    @Override
    public Trigger getTrigger(final TriggerKey triggerKey) throws SchedulerException {
        return scheduler.getTrigger(triggerKey);
    }

    @Override
    public Trigger.TriggerState getTriggerState(final TriggerKey triggerKey) throws SchedulerException {
        return scheduler.getTriggerState(triggerKey);
    }

    @Override
    public void resetTriggerFromErrorState(final TriggerKey triggerKey) throws SchedulerException {
        // TODO
    }

    @Override
    public void addCalendar(final String s, final Calendar calendar, final boolean b, final boolean b1) throws SchedulerException {
        scheduler.addCalendar(s, calendar, b, b1);
    }

    @Override
    public boolean deleteCalendar(final String s) throws SchedulerException {
        return scheduler.deleteCalendar(s);
    }

    @Override
    public Calendar getCalendar(final String s) throws SchedulerException {
        return scheduler.getCalendar(s);
    }

    @Override
    public List<String> getCalendarNames() throws SchedulerException {
        return scheduler.getCalendarNames();
    }

    @Override
    public boolean interrupt(final JobKey jobKey) throws UnableToInterruptJobException {
        return scheduler.interrupt(jobKey);
    }

    @Override
    public boolean interrupt(final String s) throws UnableToInterruptJobException {
        return scheduler.interrupt(s);
    }

    @Override
    public boolean checkExists(final JobKey jobKey) throws SchedulerException {
        return scheduler.checkExists(jobKey);
    }

    @Override
    public boolean checkExists(final TriggerKey triggerKey) throws SchedulerException {
        return scheduler.checkExists(triggerKey);
    }

    @Override
    public void clear() throws SchedulerException {
        scheduler.clear();
    }
}
