package com.bordozer.jphoto.admin.jobs.general;

import com.bordozer.jphoto.admin.jobs.entries.AbstractJob;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.general.base.AbstractBaseEntity;
import com.bordozer.jphoto.core.interfaces.Nameable;

public class SavedJob extends AbstractBaseEntity implements Nameable {

    private String name;
    private boolean active;
    private SavedJobType jobType;

    private AbstractJob job;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public void setJobType(final SavedJobType jobType) {
        this.jobType = jobType;
    }

    public SavedJobType getJobType() {
        return jobType;
    }

    public AbstractJob getJob() {
        return job;
    }

    public void setJob(final AbstractJob job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", jobType.getName(), name);
    }
}
