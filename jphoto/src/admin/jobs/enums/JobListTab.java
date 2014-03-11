package admin.jobs.enums;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public enum JobListTab {
	TEMPLATES( "templates", "Job templates" )
	, ALL_SAVED_JOBS( "all", "All saved jobs" )
	, SYSTEM_JOBS( "system", "System" )
	, RECALCULATION( "recalculation", "Recalculation" )
	, OTHER_DATA_GENERATION( "datageneration", "Data generation / Import" )
	, PHOTO_ACTIONS( "photoactions", "Photo actions" )
	, OTHER( "other", "User actions / Favorites" )
	, CLEAN_UP( "cleanup", "Clean-up" )
	, CHAINS( "chains", "Chains" )
	, JOB_EXECUTION_HISTORY( "done", "Active / Performed" )
	;

	private final String key;
	private final String name;

	public final static List<JobListTab> SAVED_JOB_TABS = newArrayList( SYSTEM_JOBS, RECALCULATION, OTHER_DATA_GENERATION, PHOTO_ACTIONS, OTHER, CLEAN_UP, CHAINS );

	private JobListTab( final String key, final String name ) {
		this.key = key;
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public static JobListTab getByKey( final String key ) {
		for ( final JobListTab tab : JobListTab.values() ) {
			if ( tab.getKey().equals( key ) ) {
				return tab;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal JobListTab: %s", key ) );
	}
}
