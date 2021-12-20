package com.bordozer.jphoto.admin.jobs.general;

import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;

import java.util.Date;

public class JobRuntimeLog {

    private final TranslatableMessage translatableMessage;
    private final Date jobRuntimeLogEntryTime;

    public JobRuntimeLog(final TranslatableMessage translatableMessage, final Date jobRuntimeLogEntryTime) {
        this.translatableMessage = translatableMessage;
        this.jobRuntimeLogEntryTime = jobRuntimeLogEntryTime;
    }

    public TranslatableMessage getTranslatableMessage() {
        return translatableMessage;
    }

    public Date getJobRuntimeLogEntryTime() {
        return jobRuntimeLogEntryTime;
    }
}
