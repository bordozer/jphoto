package com.bordozer.jphoto.admin.jobs.general;

import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.utils.NumberUtils;

public class JobProgressDTO {

    private int current;
    private int total;
    private int percent;
    private int jobStatusId;
    private boolean jobActive;
    private String jobExecutionDuration;

    private TranslatorService translatorService;
    private Language language;

    public JobProgressDTO(final TranslatorService translatorService, final Language language) {
        this.translatorService = translatorService;
        this.language = language;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(final int current) {
        this.current = current;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(final int total) {
        this.total = total;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(final int percent) {
        this.percent = percent;
    }

    public int getJobStatusId() {
        return jobStatusId;
    }

    public void setJobStatusId(final int jobStatusId) {
        this.jobStatusId = jobStatusId;
    }

    public boolean isJobActive() {
        return jobActive;
    }

    public void setJobActive(final boolean jobActive) {
        this.jobActive = jobActive;
    }

    public String getJobExecutionDuration() {
        return jobExecutionDuration;
    }

    public void setJobExecutionDuration(final String jobExecutionDuration) {
        this.jobExecutionDuration = jobExecutionDuration;
    }

    public String getProgressStatusFullDescription() {
        final StringBuilder builder = new StringBuilder();

        final double percentage = getJobExecutionPercentage();

        builder.append(current).append(" / ").append(total > 0 ? total : translatorService.translate("Job steps qty: Calculating...", language));
        builder.append(", ").append(percentage).append("%, ");
        builder.append(jobExecutionDuration);

        return builder.toString();
    }

    public int getJobExecutionPercentage() {
        if (total == 0) {
            return 0;
        }

        return NumberUtils.floor(100 * current / total);
    }
}
