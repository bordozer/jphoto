package com.bordozer.jphoto.admin.jobs.loaders;

import com.bordozer.jphoto.admin.jobs.enums.JobListTab;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.admin.jobs.general.SavedJob;
import com.bordozer.jphoto.admin.services.jobs.SavedJobService;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ByGroupJobsLoader extends AbstractSavedJobsLoader {

    final JobListTab jobListTab;

    protected ByGroupJobsLoader(final JobListTab jobListTab, final SavedJobService savedJobService) {
        super(savedJobService);
        this.jobListTab = jobListTab;
    }

    @Override
    public List<SavedJob> load() {
        final List<SavedJob> savedJobs = newArrayList(savedJobService.loadAll());
        CollectionUtils.filter(savedJobs, new Predicate<SavedJob>() {
            @Override
            public boolean evaluate(final SavedJob savedJob) {
                final SavedJobType jobType = savedJob.getJobType();

                return jobType != null && jobType.getJobListTab() == jobListTab;

            }
        });

        return savedJobs;
    }
}
