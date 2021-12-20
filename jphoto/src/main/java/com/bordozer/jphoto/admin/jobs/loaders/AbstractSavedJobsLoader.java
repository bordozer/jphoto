package com.bordozer.jphoto.admin.jobs.loaders;

import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.admin.jobs.general.SavedJob;
import com.bordozer.jphoto.admin.services.jobs.SavedJobService;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public abstract class AbstractSavedJobsLoader {

    protected final SavedJobService savedJobService;

    public abstract List<SavedJob> load();

    protected AbstractSavedJobsLoader(final SavedJobService savedJobService) {
        this.savedJobService = savedJobService;
    }

    public List<SavedJob> load(final SavedJobType jobType) {
        final List<SavedJob> savedJobs = load();

        CollectionUtils.filter(savedJobs, new Predicate<SavedJob>() {
            @Override
            public boolean evaluate(final SavedJob savedJob) {
                return savedJob.getJobType() == jobType;
            }
        });

        return savedJobs;
    }
}
