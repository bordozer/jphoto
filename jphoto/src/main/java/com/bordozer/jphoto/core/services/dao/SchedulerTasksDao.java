package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.enums.SchedulerTaskProperty;
import com.bordozer.jphoto.core.general.scheduler.SchedulerTask;

import java.util.List;

public interface SchedulerTasksDao extends BaseEntityDao<SchedulerTask> {

    List<SchedulerTask> loadAll();

    int loadIdByName(final String schedulerTaskName);

    boolean saveProperties(final int schedulerTaskId, final SchedulerTaskProperty key, final String value);

    String getSchedulerTaskPropertyValue(final int schedulerTaskId, final SchedulerTaskProperty key);

    void deleteProperties(final int schedulerTaskId);

    boolean isJobScheduled(final int savedJobId);
}
