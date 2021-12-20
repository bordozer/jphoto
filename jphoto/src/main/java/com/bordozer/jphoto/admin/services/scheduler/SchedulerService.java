package com.bordozer.jphoto.admin.services.scheduler;

import com.bordozer.jphoto.core.general.scheduler.SchedulerTask;
import com.bordozer.jphoto.core.interfaces.AllEntriesLoadable;
import com.bordozer.jphoto.core.interfaces.BaseEntityService;
import org.quartz.SchedulerException;

import java.util.List;

public interface SchedulerService extends BaseEntityService<SchedulerTask>, AllEntriesLoadable<SchedulerTask> {

    int loadIdByName(final String schedulerTaskName);

    boolean isJobScheduled(final int savedJobId);

    void deactivateSchedulerTasks(final List<Integer> ids) throws SchedulerException;

    void activateSchedulerTasks(final List<Integer> ids) throws SchedulerException;

    void activateSchedulerTask(final int schedulerTaskId) throws SchedulerException;

    void deactivateSchedulerTask(final int schedulerTaskId) throws SchedulerException;
}
