package admin.jobs.loaders;

import admin.jobs.general.SavedJob;
import admin.services.jobs.SavedJobService;

import java.util.List;

public class AllJobsLoader extends AbstractSavedJobsLoader {

	protected AllJobsLoader( final SavedJobService savedJobService ) {
		super( savedJobService );
	}

	@Override
	public List<SavedJob> load() {
		return savedJobService.loadAll();
	}
}
