package admin.jobs.loaders;

import admin.jobs.general.SavedJob;
import admin.jobs.enums.SavedJobType;
import core.services.job.SavedJobService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;

import java.util.List;

public abstract class AbstractSavedJobsLoader {

	protected final SavedJobService savedJobService;

	public abstract List<SavedJob> load();

	protected AbstractSavedJobsLoader( final SavedJobService savedJobService ) {
		this.savedJobService = savedJobService;
	}

	public List<SavedJob> load( final SavedJobType jobType ) {
		final List<SavedJob> savedJobs = load();

		CollectionUtils.filter( savedJobs, new Predicate<SavedJob>() {
			@Override
			public boolean evaluate( final SavedJob savedJob ) {
				return savedJob.getJobType() == jobType;
			}
		} );

		return savedJobs;
	}
}
