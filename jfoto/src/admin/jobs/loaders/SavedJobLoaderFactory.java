package admin.jobs.loaders;

import admin.services.jobs.JobExecutionHistoryService;
import admin.jobs.enums.JobListTab;
import admin.jobs.enums.SavedJobType;
import admin.jobs.general.TabJobInfo;
import admin.services.jobs.SavedJobService;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class SavedJobLoaderFactory {

	protected final SavedJobService savedJobService;
	protected final JobExecutionHistoryService jobExecutionHistoryService;

	public SavedJobLoaderFactory( final SavedJobService savedJobService, final JobExecutionHistoryService jobExecutionHistoryService ) {
		this.savedJobService = savedJobService;
		this.jobExecutionHistoryService = jobExecutionHistoryService;
	}

	public AbstractSavedJobsLoader getInstance( final JobListTab jobListTab ) {
		switch ( jobListTab ) {
			case ALL_SAVED_JOBS:
				return new AllJobsLoader( savedJobService );
			default:
				return new ByGroupJobsLoader( jobListTab, savedJobService );
		}

//		throw new IllegalArgumentException( String.format( "Not supported JobListTab: %s", jobListTab ) );
	}

	public Map<JobListTab, TabJobInfo> getTabJobInfos() {
		final SavedJobLoaderFactory loaderFactory = new SavedJobLoaderFactory( savedJobService, jobExecutionHistoryService );

		final Map<JobListTab, TabJobInfo> tabJobInfos = newHashMap();

		tabJobInfos.put( JobListTab.TEMPLATES, new TabJobInfo( JobListTab.TEMPLATES, SavedJobType.values().length ) );
		tabJobInfos.put( JobListTab.ALL_SAVED_JOBS, new TabJobInfo( JobListTab.ALL_SAVED_JOBS, loaderFactory.getInstance( JobListTab.ALL_SAVED_JOBS ).load().size() ) );

		for ( final JobListTab savedJobTab : JobListTab.SAVED_JOB_TABS ) {
			tabJobInfos.put( savedJobTab, new TabJobInfo( savedJobTab, loaderFactory.getInstance( savedJobTab ).load().size() ) );
		}



		tabJobInfos.put( JobListTab.JOB_EXECUTION_HISTORY, new TabJobInfo( JobListTab.JOB_EXECUTION_HISTORY, jobExecutionHistoryService.getJobExecutionHistoryEntries().size() ) ); // TODO

		return tabJobInfos;
	}
}
