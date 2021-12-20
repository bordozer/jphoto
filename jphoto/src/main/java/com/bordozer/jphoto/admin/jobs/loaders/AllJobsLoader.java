package com.bordozer.jphoto.admin.jobs.loaders;

import com.bordozer.jphoto.admin.jobs.general.SavedJob;
import com.bordozer.jphoto.admin.services.jobs.SavedJobService;

import java.util.List;

public class AllJobsLoader extends AbstractSavedJobsLoader {

    protected AllJobsLoader(final SavedJobService savedJobService) {
        super(savedJobService);
    }

    @Override
    public List<SavedJob> load() {
        return savedJobService.loadAll();
    }
}
