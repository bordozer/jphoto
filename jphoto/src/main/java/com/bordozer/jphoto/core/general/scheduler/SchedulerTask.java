package com.bordozer.jphoto.core.general.scheduler;

import com.bordozer.jphoto.core.general.base.AbstractBaseEntity;
import com.bordozer.jphoto.core.general.executiontasks.AbstractExecutionTask;
import com.bordozer.jphoto.core.general.executiontasks.ExecutionTaskType;
import com.bordozer.jphoto.core.interfaces.Nameable;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;

public class SchedulerTask extends AbstractBaseEntity implements Nameable {

    private String name;
    private ExecutionTaskType taskType;
    private int savedJobId;

    private AbstractExecutionTask executionTask;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ExecutionTaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(final ExecutionTaskType taskType) {
        this.taskType = taskType;
    }

    public int getSavedJobId() {
        return savedJobId;
    }

    public void setSavedJobId(final int savedJobId) {
        this.savedJobId = savedJobId;
    }

    public AbstractExecutionTask getExecutionTask() {
        return executionTask;
    }

    public void setExecutionTask(final AbstractExecutionTask executionTask) {
        this.executionTask = executionTask;
    }

    public TranslatableMessage getDescription() {
        return executionTask.getDescription();
    }
}
